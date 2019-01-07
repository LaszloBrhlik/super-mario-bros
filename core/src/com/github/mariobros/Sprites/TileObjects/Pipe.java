package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.Mario;

public class Pipe extends InteractiveTileObject {
  public Pipe(PlayScreen screen, MapObject object) {
    super(screen, object);
  }

  @Override
  public void onHeadHit(Mario mario) {

  }

  @Override
  public void fixtureDefFilterCategoryBits() {
    fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
  }
}
