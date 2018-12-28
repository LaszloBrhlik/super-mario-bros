package com.github.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Scenes.Hud;
import com.github.mariobros.Screens.PlayScreen;

public class Coin extends InteractiveTileObject {
  private TiledMapTileSet tileSet;
  private final int BLANK_COIN_INDEX = 28;
  private final int COIN_SCORE = 100;

  public Coin(PlayScreen screen, Rectangle bounds) {
    super(screen, bounds);
    tileSet = map.getTileSets().getTileSet("tileset_gutter");
    fixture.setUserData(this);
    setCategoryFilter(MarioBros.COIN_BIT);
  }

  @Override
  public void onHeadHit() {
    Gdx.app.log("coin", "collision");
    playSound();
    addScore();
    getCell().setTile(tileSet.getTile(BLANK_COIN_INDEX));
  }

  public void playSound() {
    if (getCell().getTile().getId() == BLANK_COIN_INDEX) {
      MarioBros.assetManager.get("audio/sounds/bump.wav", Sound.class).play();
    } else {
      MarioBros.assetManager.get("audio/sounds/coin.wav", Sound.class).play();
    }
  }

  private void addScore() {
    if (getCell().getTile().getId() != BLANK_COIN_INDEX) {
      Hud.addScore(COIN_SCORE);
    }
  }

  @Override
  public void fixtureDefFilterCategoryBits() {

  }
}
