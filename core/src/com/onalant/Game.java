package com.onalant;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
    ShapeRenderer renderer;
	Board board;
    List<String> words = new ArrayList<String>();
	
	@Override
	public void create() {
		batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        board = new Board("abcdefghijklmnop");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("wordlist.txt")));
            String word = null;
            while((word = reader.readLine()) != null)
                words.add(word);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                board.resetWrong();
                Board.Tile tile = board.onTile(screenX, screenY);
                if(tile != null) {
                    Board.Tile.current += tile.c;
                    return (tile.isActive = true);
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                int index = Collections.binarySearch(words, Board.Tile.current);
                for(int i = 0; i < 4; i++)
                    for (int j = 0; j < 4; j++)
                        if (board.values[i][j].isActive)
                            if(index < 1)
                                board.values[i][j].wrong = 2;
                            else
                                board.values[i][j].wrong = 1;
                board.reset();
                Board.Tile.current = "";
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                Board.Tile tile = board.onTile(screenX, screenY);
                if(tile != null) {
                    Board.Tile.current += tile.c;
                    return (tile.isActive = true);
                }
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        board.render(renderer);
		batch.end();
	}
}
