package com.github.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.Mario;

public abstract class Enemy extends Sprite {
  protected World world;
  protected PlayScreen screen;
  protected BodyDef bdef;
  protected FixtureDef fdef;
  protected boolean toDestroy;
  protected boolean destroyed;
  protected float stateTime;
  protected Animation walkAnimation;
  protected Array<TextureRegion> frames;

  public Body body;
  public Vector2 velocity;


  public Enemy(PlayScreen screen, float x, float y) {
    this.world = screen.getWorld();
    this.screen = screen;
    this.toDestroy = false;
    this.destroyed = false;
    setPosition(x, y);
    defineEnemy();
    this.velocity = new Vector2(0.5f, 0);
    body.setActive(false);
  }

  protected abstract void defineEnemy();
  public abstract void hitOnHead(Mario mario);
  public abstract void update(float dt);
  public abstract void onEnemyHit(Enemy enemy);

  public void reverseVelocity(boolean x, boolean y) {
    if (x) velocity.x = -velocity.x;
    if (y) velocity.y = -velocity.y;
  }
}
