package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Scenes.Hud;
import com.github.mariobros.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {
  private final int BRICK_SCORE = 200;

  public Brick(PlayScreen screen, Rectangle bounds) {
    super(screen, bounds);
    fixture.setUserData(this);
    setCategoryFilter(MarioBros.BRICK_BIT);
  }

  @Override
  public void onHeadHit() {
    setCategoryFilter(MarioBros.DESTROYED_BIT);
    getCell().setTile(null);
    Hud.addScore(BRICK_SCORE);
    MarioBros.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();
  }

  @Override
  public void fixtureDefFilterCategoryBits() {

  }
}
