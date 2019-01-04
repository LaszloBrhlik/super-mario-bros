package com.github.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.github.mariobros.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
  protected World world;
  protected PlayScreen screen;
  protected BodyDef bdef;
  public Body b2body;
  protected FixtureDef fdef;
  protected boolean setToDestroy;
  protected boolean destroyed;

  public Enemy(PlayScreen screen, float x, float y) {
    this.world = screen.getWorld();
    this.screen = screen;
    this.setToDestroy = false;
    this.destroyed = false;
    setPosition(x, y);
    defineEnemy();
  }

  protected abstract void defineEnemy();

  public abstract void hitOnHead();
}
