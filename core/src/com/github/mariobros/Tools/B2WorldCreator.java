package com.github.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.github.mariobros.Sprites.Brick;
import com.github.mariobros.Sprites.Candy;
import com.github.mariobros.Sprites.Coin;
import com.github.mariobros.Sprites.Ground;
import com.github.mariobros.Sprites.Pipe;

public class B2WorldCreator {

  public B2WorldCreator(World world, TiledMap map) {

    //create ground bodies/fixtures
    for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Ground(world, map, rect);
    }

    //create pipe bodies/fixtures
    for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Pipe(world, map, rect);
    }

    //create coins bodies/fixtures
    for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Coin(world, map, rect);
    }

    //create bricks bodies/fixtures
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Brick(world, map, rect);
    }

    //create candy bodies/fixtures
    for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Candy(world, map, rect);
    }
  }
}
