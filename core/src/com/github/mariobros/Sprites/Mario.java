package com.github.mariobros.Sprites;

import com.badlogic.gdx.audio.Sound;
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
  public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING}

  private static final int MARIO_STARTING_POS_X = 32;
  private static final int MARIO_STARTING_POS_Y = 32;

  private static final int MARIO_RUN_SPRITE_DURATION = 4;
  private static final int MARIO_SPRITE_POS_X = 0;
  private static final int MARIO_SPRITE_POS_Y = 0;
  private static final int MARIO_JUMP_SPRITE_NR_X = 5;
  private static final int MARIO_GROW_HALF_SPRITE_NR_X = 15;
  private static final int MARIO_SPRITE_WIDTH = 16;
  private static final int MARIO_SMALL_SPRITE_HEIGHT = 16;
  private static final int MARIO_BIG_SPRITE_HEIGHT = 32;
  private static final int MARIO_INCREASE_SIZE_Y = 10;
  private static final int MARIO_BIG_EXTEND_SHAPE = -14;
  private static final int MARIO_BIG_EXTEND_POSITION = -6;
  private static final float FRAME_DURATION = 0.1f;
  private static final float MARIO_SHAPE_RADIUS = 6.5f;
  private static final float MARIO_HEAD_EDGE_X = 2;
  private static final float MARIO_HEAD_EDGE_Y = 6;

  public State currentState;
  public State previousState;

  public World world;
  public Body b2body;
  public PlayScreen screen;

  private TextureRegion marioStand;
  private Animation marioRun;
  private TextureRegion marioJump;
  private TextureRegion bigMarioStand;
  private TextureRegion bigMarioJump;
  private Animation bigMarioRun;
  private Animation growMario;

  private boolean runningRight;
  private float stateTimer;
  private boolean runGrowAnimation;
  private boolean timeToDefineBigMario;
  private boolean timeToRedefineMario;

  public boolean marioIsBig;

  public Mario(PlayScreen screen) {
    this.world = screen.getWorld();
    this.screen = screen;
    currentState = State.STANDING;
    previousState = State.STANDING;
    stateTimer = 0;
    runningRight = true;

    Array<TextureRegion> frames = new Array<TextureRegion>();
    addMarioRunAnimation(frames);
    addMarioJumpAnimation(frames);
    addMarioStandAnimation();
    addMarioGrowAnimation(frames);

    defineMario();
  }

  private void addMarioStandAnimation() {
    marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), MARIO_SPRITE_POS_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_SMALL_SPRITE_HEIGHT);
    bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), MARIO_SPRITE_POS_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT);
  }

  private void addMarioRunAnimation(Array<TextureRegion> frames) {
    for (int i = 0; i < MARIO_RUN_SPRITE_DURATION; i++) {
      frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * MARIO_SPRITE_WIDTH, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_SMALL_SPRITE_HEIGHT));
    }
    marioRun = new Animation(FRAME_DURATION, frames);
    frames.clear();
    for (int i = 0; i < MARIO_RUN_SPRITE_DURATION; i++) {
      frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * MARIO_SPRITE_WIDTH, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT));
    }
    bigMarioRun = new Animation(FRAME_DURATION, frames);
    frames.clear();
  }

  private void addMarioJumpAnimation(Array<TextureRegion> frames) {
    marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), MARIO_SPRITE_WIDTH * MARIO_JUMP_SPRITE_NR_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_SMALL_SPRITE_HEIGHT);
    bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), MARIO_SPRITE_WIDTH * MARIO_JUMP_SPRITE_NR_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT);
    frames.clear();
  }

  private void addMarioGrowAnimation(Array<TextureRegion> frames) {
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), MARIO_SPRITE_WIDTH * MARIO_GROW_HALF_SPRITE_NR_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), MARIO_SPRITE_POS_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), MARIO_SPRITE_WIDTH * MARIO_GROW_HALF_SPRITE_NR_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT));
    frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), MARIO_SPRITE_POS_X, MARIO_SPRITE_POS_Y, MARIO_SPRITE_WIDTH, MARIO_BIG_SPRITE_HEIGHT));
    growMario = new Animation(FRAME_DURATION, frames);
    frames.clear();
  }

  public void grow() {
    marioIsBig = true;
    runGrowAnimation = true;
    timeToDefineBigMario = true;
    setBounds(getX(), getY(), getWidth(), getHeight() * 2);
    MarioBros.assetManager.get("audio/sounds/powerup.wav", Sound.class).play();
  }

  public void hit() {
    if (marioIsBig) {
      marioIsBig = false;
      timeToRedefineMario = true;
      setBounds(getX(), getY(), getWidth(), getHeight() / 2);
      MarioBros.assetManager.get("audio/sounds/powerdown.wav", Sound.class).play();
    } else {
      MarioBros.assetManager.get("audio/sounds/mariodie.wav", Sound.class).play();
    }
  }

  private void defineMario() {
    BodyDef bdef = new BodyDef();
    bdef.position.set(MARIO_STARTING_POS_X / MarioBros.PPM, MARIO_STARTING_POS_Y / MarioBros.PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(MARIO_SHAPE_RADIUS / MarioBros.PPM);
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

    setBounds(0, 0, MARIO_SPRITE_WIDTH / MarioBros.PPM, MARIO_SMALL_SPRITE_HEIGHT / MarioBros.PPM);
    setRegion(marioStand);

    EdgeShape head = new EdgeShape();
    head.set(new Vector2(-MARIO_HEAD_EDGE_X / MarioBros.PPM, MARIO_HEAD_EDGE_Y / MarioBros.PPM), new Vector2(MARIO_HEAD_EDGE_X / MarioBros.PPM, MARIO_HEAD_EDGE_Y / MarioBros.PPM));
    fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
    fdef.shape = head;
    fdef.isSensor = true;

    b2body.createFixture(fdef).setUserData(this);
  }

  public void defineBigMario() {
    Vector2 currentPosition = b2body.getPosition();
    world.destroyBody(b2body);

    BodyDef bdef = new BodyDef();
    bdef.position.set(currentPosition.add(0, MARIO_INCREASE_SIZE_Y / MarioBros.PPM));
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(MARIO_SHAPE_RADIUS / MarioBros.PPM);
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
    shape.setPosition(new Vector2(0, MARIO_BIG_EXTEND_SHAPE / MarioBros.PPM));
    b2body.createFixture(fdef).setUserData(this);

    EdgeShape head = new EdgeShape();
    head.set(new Vector2(-MARIO_HEAD_EDGE_X / MarioBros.PPM, MARIO_HEAD_EDGE_Y / MarioBros.PPM), new Vector2(MARIO_HEAD_EDGE_X / MarioBros.PPM, MARIO_HEAD_EDGE_Y / MarioBros.PPM));
    fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
    fdef.shape = head;
    fdef.isSensor = true;

    b2body.createFixture(fdef).setUserData(this);
    timeToDefineBigMario = false;
  }

  public void update(float dt) {
    //update our sprite to correspond with the position of our Box2D body
    if (marioIsBig) {
      setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + MARIO_BIG_EXTEND_POSITION / MarioBros.PPM);
    } else {
      setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }
    //update sprite with the correct frame depending on mario's current action
    setRegion(getFrame(dt));
    if (timeToDefineBigMario) {
      defineBigMario();
    }
    if (timeToRedefineMario) {
      redefineMario();
    }
  }

  public void redefineMario() {
    Vector2 position = b2body.getPosition();
    world.destroyBody(b2body);

    BodyDef bdef = new BodyDef();
    bdef.position.set(position);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(MARIO_SHAPE_RADIUS / MarioBros.PPM);
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

    //setBounds(0, 0, MARIO_SPRITE_WIDTH / MarioBros.PPM, MARIO_SMALL_SPRITE_HEIGHT / MarioBros.PPM);
    //setRegion(marioStand);

    EdgeShape head = new EdgeShape();
    head.set(new Vector2(-MARIO_HEAD_EDGE_X / MarioBros.PPM, MARIO_HEAD_EDGE_Y / MarioBros.PPM), new Vector2(MARIO_HEAD_EDGE_X / MarioBros.PPM, MARIO_HEAD_EDGE_Y / MarioBros.PPM));
    fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
    fdef.shape = head;
    fdef.isSensor = true;

    b2body.createFixture(fdef).setUserData(this);

    timeToRedefineMario = false;
  }

  private TextureRegion getFrame(float dt) {
    currentState = getState();

    TextureRegion region;
    switch (currentState) {
      case GROWING:
        region = (TextureRegion) growMario.getKeyFrame(stateTimer);
        if (growMario.isAnimationFinished(stateTimer)) {
          runGrowAnimation = false;
        }
        break;
      case JUMPING:
        region = marioIsBig ? bigMarioJump : marioJump;
        break;
      case RUNNING:
        region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
        break;
      case FALLING:
      case STANDING:
      default:
        region = marioIsBig ? bigMarioStand : marioStand;
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
    if (runGrowAnimation) {
      return State.GROWING;
    } else if (b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) {
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