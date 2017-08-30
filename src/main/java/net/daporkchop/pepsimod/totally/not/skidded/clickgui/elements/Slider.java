package net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;

import java.text.DecimalFormat;

public class Slider {

    private Window window;
    private ModuleOption sliderValue;
    private int xPos;
    private int yPos;
    private float maxValue;
    private float minValue;

    private boolean shouldRound;

    public boolean dragging;
    public float dragX, lastDragX;

    private int drawSliderWidth;
    private int sliderWidth;

    public void dragSlider(int x) {
        dragX = x - lastDragX;
    }

    public Slider(Window window, ModuleOption value, int x, int y) {
        this.window = window;
        this.sliderValue = value;
        this.xPos = x;
        this.yPos = y;
        this.maxValue = 10.0F;
        this.minValue = 0.0F;
        this.drawSliderWidth = 95;
        this.sliderWidth = this.drawSliderWidth - 5;
    }

    public Slider(Window window, ModuleOption value, int x, int y, float maxValue) {
        this.window = window;
        this.sliderValue = value;
        this.xPos = x;
        this.yPos = y;
        this.maxValue = maxValue;
        this.minValue = 0.0F;
        this.drawSliderWidth = 95;
        this.sliderWidth = this.drawSliderWidth - 5;
    }

    public Slider(Window window, ModuleOption value, int x, int y, float minValue, float maxValue, boolean shouldRound) {
        this.window = window;
        this.sliderValue = value;
        this.xPos = x;
        this.yPos = y;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.drawSliderWidth = 95;
        this.shouldRound = shouldRound;
        this.sliderWidth = this.drawSliderWidth - 5;
    }

    public void draw(int x) {
        if(dragging) {
            dragSlider(x);
        }
        if(dragX < 0) {
            dragX = 0;
        }
        if(dragX > this.sliderWidth) {
            dragX = this.sliderWidth;
        }

        DecimalFormat format = new DecimalFormat(shouldRound ? "0" : "0.00");

        PepsiMod.INSTANCE.mc.fontRenderer.drawStringWithShadow(sliderValue.getName() + ": " + format.format(sliderValue.getValue()), xPos + 1 + window.dragX, yPos - 3 + window.dragY, 0xFFFFFF);

        RenderUtilsXdolf.drawBetterBorderedRect(xPos + window.dragX, yPos + 9 + window.dragY, xPos + this.drawSliderWidth + window.dragX + 1, yPos + 17 + window.dragY, 0.5F, 0xFF000000, 0xFF383b42);
        RenderUtilsXdolf.drawBetterBorderedRect(xPos + window.dragX + 1, yPos + 10 + window.dragY, xPos + 5 + window.dragX + (int)dragX, yPos + 16 + window.dragY, 0.5F, 0xFF000000, 0xFFFF0000);
        RenderUtilsXdolf.drawBetterBorderedRect(xPos + 2 + window.dragX + (int)dragX, yPos + 10 + window.dragY, xPos + 5 + window.dragX + (int)dragX, yPos + 16 + window.dragY, 0.5F, 0xFF000000, 0xFFFF4c4c);
        //ff3232
        float fraction = this.sliderWidth / (maxValue - minValue);

        sliderValue.setValue(sliderValue.getName().equals("Step") ? ((dragX / fraction) + minValue) + 0.1F : shouldRound ? (int)(dragX / fraction) + minValue : (dragX / fraction) + minValue);
    }

    public float getValue() {
        return Float.parseFloat((new DecimalFormat("0.0")).format(this.sliderValue.getValue()));
    }

    public void setValue(float value) {
        value -= minValue;

        float fraction = 80 / (maxValue - minValue);
        sliderValue.setValue(value);
        dragX = fraction * value;
    }

    public void mouseClicked(int x, int y, int button) {
        if(button == 0) {
            if(x >= xPos + window.dragX && y >= yPos + 9 + window.dragY && x <= xPos + 6 + window.dragX + dragX && y <= yPos + 15.5F + window.dragY) {
                lastDragX = x - dragX;
                dragging = true;
            }
        }
    }

    public void mouseReleased(int x, int y, int b) {
        if(b == 0) {
            dragging = false;
        }
    }
}