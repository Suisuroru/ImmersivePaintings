package immersive_paintings.network.c2s;

import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.entity.ImmersivePaintingEntity;
import immersive_paintings.network.PaintingDataMessage;
import immersive_paintings.network.s2c.PaintingModifyMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PaintingModifyRequest extends PaintingDataMessage {
    public PaintingModifyRequest(ImmersivePaintingEntity painting) {
        super(painting);
    }

    public PaintingModifyRequest(PacketByteBuf b) {
        super(b);
    }

    @Override
    public void receive(PlayerEntity e) {
        Entity entity = e.getWorld().getEntityById(getEntityId());
        if (entity instanceof ImmersivePaintingEntity painting) {
            if (e.getUuid() != painting.getOwner() && !e.hasPermissionLevel(2)) {
                e.sendMessage(Text.of("You are not allowed to edit this painting"));
                return;
            }
            painting.setMotive(getMotive());
            painting.setFrame(getFrame());
            painting.setMaterial(getMaterial());
            e.getWorld().getPlayers().forEach(p -> NetworkHandler.sendToPlayer(new PaintingModifyMessage(painting), (ServerPlayerEntity)p));
        }
    }
}
