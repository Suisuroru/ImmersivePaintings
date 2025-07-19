package immersive_paintings.network.c2s;

import immersive_paintings.cobalt.network.Message;
import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.entity.ImmersivePaintingEntity;
import immersive_paintings.network.s2c.PaintingModifyMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class EditRequest extends Message {
    private final int entityId;
    private double rotate;
    private final double x;
    private final double y;
    private final double z;

    public EditRequest(ImmersivePaintingEntity painting, double rotate, double x, double y, double z) {
        this.entityId = painting.getId();
        this.rotate = rotate;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EditRequest(PacketByteBuf b) {
        entityId = b.readInt();
        rotate = b.readDouble();
        x = b.readDouble();
        y = b.readDouble();
        z = b.readDouble();
    }

    public void encode(PacketByteBuf b) {
        b.writeInt(entityId);
        b.writeDouble(rotate);
        b.writeDouble(x);
        b.writeDouble(y);
        b.writeDouble(z);
    }

    @Override
    public void receive(PlayerEntity e) {
        e.sendMessage(Text.of("received edit request, data " + rotate + " " + x + " " + y + " " + z));
        Entity entity = e.getWorld().getEntityById(entityId);
        if (entity instanceof ImmersivePaintingEntity painting) {
            if (e.getUuid() != painting.getOwner() && !e.hasPermissionLevel(2)) {
                e.sendMessage(Text.of("You are not allowed to edit this painting"));
                return;
            }
            rotate = rotate % 360;
            if (rotate < 0) rotate += 360;
            e.sendMessage(Text.of("accepted edit request, data " + rotate + " " + x + " " + y + " " + z));
            painting.setValues(rotate, x, y, z);
            e.getWorld().getPlayers().forEach(p -> NetworkHandler.sendToPlayer(new PaintingModifyMessage(painting), (ServerPlayerEntity) p));
        }
    }
}
