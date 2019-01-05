package com.github.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;

public class Goomba extends Enemy {
  private static final float goombaShapeRadius = 6.5f;
  private static final int GOOMBA_SPRITE_WIDTH = 16;
  private static final int GOOMBA_SPRITE_HEIGHT = 16;
  private static final int GOOMBA_SPRITE_Y = 0;
  private static final int GOOMBA_SPRITE_NR = 2;

  private float stateTime;
  private Animation walkAnimation;
  private Array<TextureRegion> frames;

  public Goomba(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    addAnimation();
    setBounds();
  }

  private void addAnimation() {
    frames = new Array<TextureRegion>();
    for (int i = 0; i < GOOMBA_SPRITE_NR; i++) {
      frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * GOOMBA_SPRITE_WIDTH, GOOMBA_SPRITE_Y, GOOMBA_SPRITE_WIDTH, GOOMBA_SPRITE_HEIGHT));
    }
    walkAnimation = new Animation(0.4f, frames);
    stateTime = 0;
  }

  private void setBounds() {
    setBounds(getX(), getY(), GOOMBA_SPRITE_WIDTH / MarioBros.PPM, GOOMBA_SPRITE_HEIGHT / MarioBros.PPM);
  }

  public void update(float dt) {
    stateTime += dt;
    if (toDestroy && !destroyed) {
      world.destroyBody(body);
      destroyed = true;
      setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
      stateTime = 0;
    } else if (!destroyed) {
      body.setLinearVelocity(velocity);
      setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
      setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
    }
  }

  @Override
  protected void defineEnemy() {
    bdef = new BodyDef();
    bdef.position.set(getX(), getY());
    bdef.type = BodyDef.BodyType.DynamicBody;
    body = world.createBody(bdef);

    fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(goombaShapeRadius / MarioBros.PPM);
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

  public void draw(Batch batch) {
    if (!destroyed || stateTime < 1) {
      super.draw(batch);
    }
  }

  @Override
  public void hitOnHead() {
    toDestroy = true;
  }
}
