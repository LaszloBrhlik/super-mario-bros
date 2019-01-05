package com.github.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Scenes.Hud;
import com.github.mariobros.Screens.PlayScreen;
import com.github.mariobros.Sprites.Items.ItemDef;
import com.github.mariobros.Sprites.Items.Mushroom;

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
      screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + MarioBros.TILE_SIZE / MarioBros.PPM), Mushroom.class));
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
