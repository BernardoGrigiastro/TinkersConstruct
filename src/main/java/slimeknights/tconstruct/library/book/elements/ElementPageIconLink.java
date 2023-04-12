package slimeknights.tconstruct.library.book.elements;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.client.book.action.StringActionProcessor;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.screen.book.element.SizedBookElement;

@OnlyIn(Dist.CLIENT)
public class ElementPageIconLink extends SizedBookElement {
    
    public PageData pageData;
    public SizedBookElement displayElement;
    public TextData link;
    public String action;
    public String name;
    
    public ElementPageIconLink(int x, int y, SizedBookElement displayElement, String name, PageData pageData) {
        this(x, y, displayElement.width, displayElement.height, displayElement, name, pageData);
    }
    
    public ElementPageIconLink(int x, int y, int w, int h, SizedBookElement displayElement, String name, PageData pageData) {
        super(x, y, w, h);
        this.displayElement = displayElement;
        this.pageData = pageData;
        
        this.action = "go-to-page-rtn:" + pageData.parent.name + "." + pageData.name;
        this.name = name;
    }
    
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks, TextRenderer fontRenderer) {
        boolean hover = this.isHovered(mouseX, mouseY);
        GlStateManager.color4f(1F, 1F, 1F, hover ? 1F : 0.5F);
        //GlStateManager.scale(scale, scale, 1f);
        if (this.isHovered(mouseX, mouseY)) {
            fill(this.x, this.y, this.x + this.width, this.y + this.height, this.parent.book.appearance.hoverColor | (0x77 << 24));
        }
        
        this.displayElement.draw(mouseX, mouseY, partialTicks, fontRenderer);
        //GlStateManager.scale(1/scale, 1/scale, 1f);
    }
    
    @Override
    public void drawOverlay(int mouseX, int mouseY, float partialTicks, TextRenderer fontRenderer) {
        if (this.name != null && !this.name.isEmpty() && this.isHovered(mouseX, mouseY)) {
            this.drawHoveringText(ImmutableList.of(this.name), mouseX, mouseY, fontRenderer);
        }
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            StringActionProcessor.process(this.action, this.parent);
        }
    }
}
