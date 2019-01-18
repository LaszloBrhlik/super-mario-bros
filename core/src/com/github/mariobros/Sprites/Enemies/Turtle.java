package com.github.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;

public class Turtle extends Enemy {
  private static final float turtleSpeed = 0.2f;
  private static final float TURTLE_SHAPE_RADIUS = 6.5f;

  private TextureRegion shell;

  public enum State {WALKING, SHELL}

  public State currentState;
  public State previousState;


  public Turtle(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    defineWalkAnimation();
    defineShellFrame();
    currentState = previousState = State.WALKING;
    setBounds();

  }

  private void defineWalkAnimation() {
    frames = new Array<TextureRegion>();
    frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
    walkAnimation = new Animation(turtleSpeed, frames);
  }

  private void defineShellFrame() {
    shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
  }

  private void setBounds() {
    setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);
  }

  @Override
  protected void defineEnemy() {
    bdef = new BodyDef();
    bdef.position.set(getX(), getY());
    bdef.type = BodyDef.BodyType.DynamicBody;
    body = world.createBody(bdef);

    fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(TURTLE_SHAPE_RADIUS / MarioBros.PPM);
    fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
    fdef.filter.maskBits = MarioBros.GROUND_BIT |
        MarioBros.COIN_BIT |
        MarioBros.BRICK_BIT |
        MarioBros.OBJECT_BIT |
        MarioBros.ENEMY_BIT |
        MarioBros.MARIO_BIT;

    fdef.shape = shape;
    body.createFixture(fdef).setUserData(this);

    //create the head here:
    PolygonShape head = new PolygonShape();
    Vector2[] vertice = new Vector2[4];
    vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
    vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
    vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
    vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
    head.set(vertice);

    fdef.shape = head;
    fdef.restitution = 0.5f;
    fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
    body.createFixture(fdef).setUserData(this);
  }

  @Override
  public void hitOnHead() {
    if (currentState != State.SHELL) {
      currentState = State.SHELL;
      velocity.x = 0;
    }
  }

  @Override
  public void update(float dt) {
    setRegion(getFrame(dt));
    if (currentState == State.SHELL && stateTime > 5) {
      currentState = State.WALKING;
      velocity.x = 1;
    }
    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 8 / MarioBros.PPM);
    body.setLinearVelocity(velocity);
  }

  public TextureRegion getFrame(float dt) {
    TextureRegion region;

    switch (currentState) {
      case SHELL:
        region = shell;
        break;
      case WALKING:
      default:
        region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
        break;
    }

    if (velocity.x > 0 && !region.isFlipX()) {
      region.flip(true, false);
    }

    if (velocity.x < 0 && region.isFlipX()) {
      region.flip(true, false);
    }

    //if the current state is the same as the previous state increase the state timer
    //otherwise the state has changed and we need to reset timer
    stateTime = currentState == previousState ? stateTime + dt : 0;
    //update previous state
    previousState = currentState;
    //return our final adjusted frame
    return region;
  }
}
