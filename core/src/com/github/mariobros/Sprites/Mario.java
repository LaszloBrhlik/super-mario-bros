package com.github.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;

public class Mario extends Sprite {
  public enum State {FALLING, JUMPING, STANDING, RUNNING}

  private static final int MARIO_STARTING_POS_X = 32;
  private static final int MARIO_STARTING_POS_Y = 32;
  private static final int MARIO_SMALL_RUN_SPRITE_END_POS = 4;
  private static final int MARIO_SMALL_JUMP_SPRITE_END_POS = 6;
  private static final int MARIO_SMALL_STAND_SPRITE_POS_X = 1;
  private static final int MARIO_SMALL_SPRITE_WIDTH = 16;
  private static final int MARIO_SMALL_SPRITE_HEIGHT = 16;
  private static final int MARIO_SMALL_SPRITE_Y = 11;
  private static final float frameDuration = 0.1f;
  private static final float marioShapeRadius = 6.5f;
  private static final float marioHeadEdgeX = 2;
  private static final float marioHeadEdgeY = 6;

  public State currentState;
  public State previousState;

  public World world;
  public Body b2body;

  private TextureRegion marioStand;

  private Animation marioRun;
  private Animation marioJump;
  private boolean runningRight;
  private float stateTimer;

  public Mario(PlayScreen screen) {
    super(screen.getAtlas().findRegion("little_mario"));
    this.world = screen.getWorld();
    currentState = State.STANDING;
    previousState = State.STANDING;
    stateTimer = 0;
    runningRight = true;

    Array<TextureRegion> frames = new Array<TextureRegion>();
    addMarioRunAnimation(frames);
    addMarioJumpAnimation(frames);
    addMarioStandAnimation();

    defineMario();
  }

  private void addMarioJumpAnimation(Array<TextureRegion> frames) {
    for (int i = MARIO_SMALL_RUN_SPRITE_END_POS; i < MARIO_SMALL_JUMP_SPRITE_END_POS; i++) {
      frames.add(new TextureRegion(getTexture(), i * MARIO_SMALL_SPRITE_WIDTH, MARIO_SMALL_SPRITE_Y, MARIO_SMALL_SPRITE_WIDTH, MARIO_SMALL_SPRITE_HEIGHT));
    }
    marioJump = new Animation(frameDuration, frames);
    frames.clear();
  }

  private void addMarioRunAnimation(Array<TextureRegion> frames) {
    for (int i = 0; i < MARIO_SMALL_RUN_SPRITE_END_POS; i++) {
      frames.add(new TextureRegion(getTexture(), i * MARIO_SMALL_SPRITE_WIDTH, MARIO_SMALL_SPRITE_Y, MARIO_SMALL_SPRITE_WIDTH, MARIO_SMALL_SPRITE_HEIGHT));
    }
    marioRun = new Animation(frameDuration, frames);
    frames.clear();
  }

  private void addMarioStandAnimation() {
    marioStand = new TextureRegion(getTexture(), MARIO_SMALL_STAND_SPRITE_POS_X, MARIO_SMALL_SPRITE_Y, MARIO_SMALL_SPRITE_WIDTH, MARIO_SMALL_SPRITE_HEIGHT);
  }


  private void defineMario() {
    BodyDef bdef = new BodyDef();
    bdef.position.set(MARIO_STARTING_POS_X / MarioBros.PPM, MARIO_STARTING_POS_Y / MarioBros.PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(marioShapeRadius / MarioBros.PPM);
    fdef.filter.categoryBits = MarioBros.MARIO_BIT;
    fdef.filter.maskBits = MarioBros.GROUND_BIT |
        MarioBros.COIN_BIT |
        MarioBros.BRICK_BIT |
        MarioBros.ENEMY_BIT |
        MarioBros.OBJECT_BIT |
        MarioBros.ENEMY_HEAD_BIT |
        MarioBros.ITEM_BIT;

    fdef.shape = shape;
    b2body.createFixture(fdef).setUserData(this);

    setBounds(0, 0, MARIO_SMALL_SPRITE_WIDTH / MarioBros.PPM, MARIO_SMALL_SPRITE_HEIGHT / MarioBros.PPM);
    setRegion(marioStand);

    EdgeShape head = new EdgeShape();
    head.set(new Vector2(-marioHeadEdgeX / MarioBros.PPM, marioHeadEdgeY / MarioBros.PPM), new Vector2(marioHeadEdgeX / MarioBros.PPM, marioHeadEdgeY / MarioBros.PPM));
    fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
    fdef.shape = head;
    fdef.isSensor = true;

    b2body.createFixture(fdef).setUserData("head");
  }

  public void update(float dt) {
    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    setRegion(getFrame(dt));
  }

  private TextureRegion getFrame(float dt) {
    currentState = getState();

    TextureRegion region;
    switch (currentState) {
      case JUMPING:
        region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
        break;
      case RUNNING:
        region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
        break;
      case FALLING:
      case STANDING:
      default:
        region = marioStand;
        break;
    }

    if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
      region.flip(true, false);
      runningRight = false;
    } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
      region.flip(true, false);
      runningRight = true;
    }

    stateTimer = currentState == previousState ? stateTimer + dt : 0;
    previousState = currentState;
    return region;
  }

  private State getState() {
    if (b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) {
      return State.JUMPING;
    } else if (b2body.getLinearVelocity().y < 0) {
      return State.FALLING;
    } else if (b2body.getLinearVelocity().x != 0) {
      return State.RUNNING;
    } else {
      return State.STANDING;
    }
  }
}