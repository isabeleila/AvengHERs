package Level;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Maps.PlayLevelMap;
import Screens.PlayLevelScreen;
import SpriteFont.SpriteFont;

import java.util.HashMap;

// This class is a base class for all npcs in the game -- all npcs should extend from it
public class NPC extends MapEntity {
    protected boolean isInteractable = false;
    protected boolean pickedUp = false; //is first aid kit picked up by a player?
    protected SpriteFont message;
    protected int talkedToTime; // how long after talking to NPC will textbox stay open -- use negative number to have it be infinite time
    protected int timer;
    protected Textbox textbox = new Textbox("");
    protected int textboxOffsetX = 0;
    protected int textboxOffsetY = 0;

    public NPC(float x, float y, SpriteSheet spriteSheet, String startingAnimation, int health) {
        super(x, y, spriteSheet, startingAnimation, health);
        this.message = createMessage();
    }

    public NPC(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        this.message = createMessage();
    }

    public NPC(float x, float y, Frame[] frames) {
        super(x, y, frames);
        this.message = createMessage();
    }

    public NPC(float x, float y, Frame frame) {
        super(x, y, frame);
        this.message = createMessage();
    }

    public NPC(float x, float y) {
        super(x, y);
        this.message = createMessage();
    }

    protected SpriteFont createMessage() {
        return null;
    }

    public void update(Player player) {
        super.update();
        checkPickedUp(player); //
        textbox.setLocation((int)getCalibratedXLocation() + textboxOffsetX, (int)getCalibratedYLocation() + textboxOffsetY);
    }

    //
    public boolean checkPickedUp(Player player) {
        if (isInteractable && intersects(player)) {
            pickedUp = true;
            //System.out.println("interacted");     DEBUG STATEMENT
            this.mapEntityStatus = MapEntityStatus.REMOVED; //removes the first aid once player interacts with it. 
            player.healPlayer(player);  //updates the health of player that interacted with it. 
            PlayLevelScreen.canSpawnItem = true;
        }
        return pickedUp;
        //LOOGIC FOR HEALTHBAAR SYNC UP GOES HERE LATER. 
        
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        if (pickedUp) {
            textbox.draw(graphicsHandler);
        }
    }
}
