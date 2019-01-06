package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;

public class Pipe extends InteractiveTileObject {
  public Pipe(PlayScreen screen, MapObject object) {
    super(screen, object);
  }

  @Override
  public void onHeadHit() {

  }

  @Override
  public void fixtureDefFilterCategoryBits() {
    fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
  }
}
