package com.github.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.Brick;
import com.github.mariobros.Sprites.Coin;
import com.github.mariobros.Sprites.Candy;
import com.github.mariobros.Sprites.Ground;
import com.github.mariobros.Sprites.Pipe;

public class B2WorldCreator {

  public B2WorldCreator(PlayScreen screen) {
    World world = screen.getWorld();
    TiledMap map = screen.getMap();

    //create ground bodies/fixtures
    for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Ground(screen, rect);
    }

    //create pipe bodies/fixtures
    for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Pipe(screen, rect);
    }

    //create coins bodies/fixtures
    for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Candy(screen, rect);
    }

    //create bricks bodies/fixtures
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Brick(screen, rect);
    }

    //create candy bodies/fixtures
    for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      new Coin(screen, rect);
    }
  }
}
