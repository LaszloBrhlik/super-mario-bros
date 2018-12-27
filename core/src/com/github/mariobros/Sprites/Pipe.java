package com.github.mariobros.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.github.mariobros.Screens.PlayScreen;

public class Pipe extends InteractiveTileObject {
  public Pipe(PlayScreen screen, Rectangle bounds) {
    super(screen, bounds);
  }

  @Override
  public void onHeadHit() {

  }
}
