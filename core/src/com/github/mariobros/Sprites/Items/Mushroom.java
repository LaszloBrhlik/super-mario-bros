package com.github.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Screens.PlayScreen;

public class Mushroom extends Item {
  public static final int MUSHROOM_TILE_POS_X = 0;
  public static final int MUSHROOM_TILE_POS_Y = 0;
  public static final int MUSHROOM_VELOCITY_X = 0;
  public static final int MUSHROOM_VELOCITY_Y = 0;

  private static final float MUSHROOM_SHAPE_RADIUS = 6.5f;

  public Mushroom(PlayScreen screen, float x, float y) {
    super(screen, x, y);
    setRegion(screen.getAtlas().findRegion("mushroom"), MUSHROOM_TILE_POS_X, MUSHROOM_TILE_POS_Y, MarioBros.TILE_SIZE, MarioBros.TILE_SIZE);
    super.velocity = new Vector2(MUSHROOM_VELOCITY_X, MUSHROOM_VELOCITY_Y);
  }

  @Override
  public void defineItem() {
    bdef = new BodyDef();
    bdef.position.set(getX(), getY());
    bdef.type = BodyDef.BodyType.DynamicBody;
    body = world.createBody(bdef);

    fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(MUSHROOM_SHAPE_RADIUS / MarioBros.PPM);

    fdef.shape = shape;
    body.createFixture(fdef).setUserData(this);
  }

  @Override
  public void use() {
    super.destroy();
  }

  @Override
  public void update(float dt) {
    super.update(dt);
    setPosition(super.body.getPosition().x - getWidth() / 2, super.body.getPosition().y - getHeight() / 2);
    super.body.setLinearVelocity(velocity);
  }
}
