package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.Mario;

public class Candy extends InteractiveTileObject {
  public Candy(PlayScreen screen, MapObject object) {
    super(screen, object);
    fixture.setUserData(this);
  }

  @Override
  public void onHeadHit(Mario mario) {
    Gdx.app.log("candy", "collision");
  }

  @Override
  public void fixtureDefFilterCategoryBits() {

  }
}
