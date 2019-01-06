package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.github.mariobros.Screens.PlayScreen;

public class Ground extends InteractiveTileObject {
  public Ground(PlayScreen screen, MapObject object) {
    super(screen, object);
  }

  @Override
  public void onHeadHit() {

  }

  @Override
  public void fixtureDefFilterCategoryBits() {

  }
}
