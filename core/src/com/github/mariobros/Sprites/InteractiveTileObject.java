package com.github.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;

public abstract class InteractiveTileObject {
  protected World world;
  protected TiledMap map;
  protected Rectangle bounds;
  protected Body body;
  protected Fixture fixture;
  protected BodyDef bdef;
  protected FixtureDef fdef;
  protected PolygonShape shape;

  public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
    this.world = screen.getWorld();
    this.map = screen.getMap();
    this.bounds = bounds;
    this.bdef = new BodyDef();
    this.fdef = new FixtureDef();
    this.shape = new PolygonShape();

    bdef.type = BodyDef.BodyType.StaticBody;
    bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM);

    body = world.createBody(bdef);

    shape.setAsBox(bounds.getWidth() / 2 / MarioBros.PPM, bounds.getHeight() / 2 / MarioBros.PPM);
    fdef.shape = shape;
    fixtureDefFilterCategoryBits();
    fixture = body.createFixture(fdef);
  }

  public abstract void onHeadHit();

  public void setCategoryFilter(short filterBit) {
    Filter filter = new Filter();
    filter.categoryBits = filterBit;
    fixture.setFilterData(filter);
  }

  public TiledMapTileLayer.Cell getCell() {
     TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
     return layer.getCell((int) (body.getPosition().x * MarioBros.PPM / MarioBros.TILE_SIZE),
         (int) (body.getPosition().y * MarioBros.PPM / MarioBros.TILE_SIZE));
  }

  public abstract void fixtureDefFilterCategoryBits();
}
