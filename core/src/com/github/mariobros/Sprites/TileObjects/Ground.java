package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.github.mariobros.Screens.PlayScreen;

public class Ground extends InteractiveTileObject {
  public Ground(PlayScreen screen, Rectangle bounds) {
    super(screen, bounds);
  }

  @Override
  public void onHeadHit() {

  }

  @Override
  public void fixtureDefFilterCategoryBits() {

  }
}