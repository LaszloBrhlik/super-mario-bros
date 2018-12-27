package com.github.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Scenes.Hud;

public class Candy extends InteractiveTileObject {
  private TiledMapTileSet tileSet;
  private final int BLANK_CANDY_INDEX = 28;

  public Candy(World world, TiledMap map, Rectangle bounds) {
    super(world, map, bounds);
    tileSet = map.getTileSets().getTileSet("tileset_gutter");
    fixture.setUserData(this);
    setCategoryFilter(MarioBros.CANDY_BIT);
  }

  @Override
  public void onHeadHit() {
    Gdx.app.log("candy", "collision");
    getCell().setTile(tileSet.getTile(BLANK_CANDY_INDEX));
    Hud.addScore(100);
  }
}
