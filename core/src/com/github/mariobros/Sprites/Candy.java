package com.github.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Candy extends InteractiveTileObject {
  public Candy(World world, TiledMap map, Rectangle bounds) {
    super(world, map, bounds);
    fixture.setUserData(this);
  }

  @Override
  public void onHeadHit() {
    Gdx.app.log("candy", "collision");
  }
}
