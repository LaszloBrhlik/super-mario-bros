package com.github.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.Mario;

public class Turtle extends Enemy {
  private static final float turtleSpeed = 0.2f;
  private static final float TURTLE_SHAPE_RADIUS = 6.5f;
  public static final int KICK_LEFT_SPEED = -2;
  public static final int KICK_RIGHT_SPEED = 2;

  private TextureRegion shell;
  private float deadRotationDegrees;

  public enum State {WALKING, STANDING_SHELL, MOVING_SHELL, DEAD}

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
    deadRotationDegrees = 0;
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
    //the measure of bouncing back when collide
    fdef.restitution = 0.5f;
    fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
    body.createFixture(fdef).setUserData(this);
  }

  @Override
  public void hitOnHead(Mario mario) {
    if (currentState != State.STANDING_SHELL) {
      currentState = State.STANDING_SHELL;
      velocity.x = 0;
    } else {
      kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
    }
  }

  public void kick(int speed) {
    velocity.x = speed;
    currentState = State.MOVING_SHELL;
  }

  @Override
  public void update(float dt) {
    setRegion(getFrame(dt));
    if (currentState == State.STANDING_SHELL && stateTime > 5) {
      currentState = State.WALKING;
      velocity.x = 0.5f;
    }
    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 8 / MarioBros.PPM);

    if (currentState == State.DEAD) {
      deadRotationDegrees += 3;
      rotate(deadRotationDegrees);
      if (stateTime > 5 && !destroyed) {
        world.destroyBody(body);
        destroyed = true;
      }
    } else {
      body.setLinearVelocity(velocity);
    }
  }

  public TextureRegion getFrame(float dt) {
    TextureRegion region;

    switch (currentState) {
      case MOVING_SHELL:
      case STANDING_SHELL:
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

  @Override
  public void onEnemyHit(Enemy enemy) {
    if (enemy instanceof Turtle) {
      if (((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
        killed();
      } else if (currentState == State.MOVING_SHELL && ((Turtle) enemy).currentState == State.WALKING) {
        return;
      } else {
        reverseVelocity(true, false);
      }
    } else if (currentState != State.MOVING_SHELL) {
      reverseVelocity(true, false);
    }
  }

  public State getCurrentState() {
    return currentState;
  }

  public void killed() {
    currentState = State.DEAD;
    Filter filter = new Filter();
    filter.maskBits = MarioBros.NOTHING_BIT;

    for (Fixture fixture : body.getFixtureList()) {
      fixture.setFilterData(filter);
    }

    body.applyLinearImpulse(new Vector2(0, 5f), body.getWorldCenter(), true);
  }

  public void draw(Batch batch) {
    if (!destroyed) {
      super.draw(batch);
    }
  }
}
