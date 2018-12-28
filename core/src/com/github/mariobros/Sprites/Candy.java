package com.github.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.github.mariobros.Screens.PlayScreen;

public class Candy extends InteractiveTileObject {
  public Candy(PlayScreen screen, Rectangle bounds) {
    super(screen, bounds);
    fixture.setUserData(this);
  }

  @Override
  public void onHeadHit() {
    Gdx.app.log("candy", "collision");
  }

  @Override
  public void fixtureDefFilterCategoryBits() {

  }
}
