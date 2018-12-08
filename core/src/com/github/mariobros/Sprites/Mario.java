package com.github.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.github.mariobros.MarioBros;

public class Mario extends Sprite {
  private static final int MARIO_STARTING_POS_X = 32;
  private static final int MARIO_STARTING_POS_Y = 32;

  public World world;
  public Body b2body;

  public Mario(World world) {
    this.world = world;
    defineMario();
  }

  private void defineMario() {
    BodyDef bdef = new BodyDef();
    bdef.position.set(MARIO_STARTING_POS_X / MarioBros.PPM, MARIO_STARTING_POS_Y / MarioBros.PPM);
    bdef.type = BodyDef.BodyType.DynamicBody;
    b2body = world.createBody(bdef);

    FixtureDef fdef = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.setRadius(5 / MarioBros.PPM);

    fdef.shape = shape;
    b2body.createFixture(fdef);
  }
}