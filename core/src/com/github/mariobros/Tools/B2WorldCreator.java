package com.github.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.TileObjects.Brick;
import com.github.mariobros.Sprites.TileObjects.Coin;
import com.github.mariobros.Sprites.TileObjects.Candy;
import com.github.mariobros.Sprites.Enemies.Goomba;
import com.github.mariobros.Sprites.TileObjects.Ground;
import com.github.mariobros.Sprites.TileObjects.Pipe;

public class B2WorldCreator {
  private Array<Goomba> goombas;

  public B2WorldCreator(PlayScreen screen) {
    World world = screen.getWorld();
    TiledMap map = screen.getMap();

    //create ground bodies/fixtures
    for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
      new Ground(screen, object);
    }

    //create pipe bodies/fixtures
    for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
      new Pipe(screen, object);
    }

    //create coins bodies/fixtures
    for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
      new Candy(screen, object);
    }

    //create bricks bodies/fixtures
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      new Brick(screen, object);
    }

    //create candy bodies/fixtures
    for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
      new Coin(screen, object);
    }

    //create all goombas
    goombas = new Array<Goomba>();
    for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
    }
  }

  public Array<Goomba> getGoombas() {
    return goombas;
  }
}
