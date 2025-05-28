package immersive_paintings.network;

import immersive_paintings.cobalt.network.Message;
import immersive_paintings.entity.ImmersivePaintingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public abstract class PaintingDataMessage extends Message {
    final String motive;
    final String frame;
    final String material;
    final int entityId;
    private final Direction facing;
    private final double rotation;
    private final double x, y, z;

    public PaintingDataMessage(ImmersivePaintingEntity painting) {
        entityId = painting.getId();
        this.motive = painting.getMotive().toString();
        this.frame = painting.getFrame().toString();
        this.material = painting.getMaterial().toString();
        this.facing = painting.getHorizontalFacing();
        this.rotation = painting.getRotation();
        this.x = painting.getAttachmentPos().getX();
        this.y = painting.getAttachmentPos().getY();
        this.z = painting.getAttachmentPos().getZ();
    }

    public PaintingDataMessage(PacketByteBuf b) {
        this.entityId = b.readInt();
        this.motive = b.readString();
        this.frame = b.readString();
        this.material = b.readString();
        this.facing = b.readEnumConstant(Direction.class);
        this.rotation = b.readDouble();
        this.x = b.readDouble();
        this.y = b.readDouble();
        this.z = b.readDouble();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeInt(entityId);
        b.writeString(motive);
        b.writeString(frame);
        b.writeString(material);
        b.writeEnumConstant(facing);
        b.writeDouble(rotation);
        b.writeDouble(x);
        b.writeDouble(y);
        b.writeDouble(z);
    }

    public Identifier getMotive() {
        return new Identifier(motive);
    }

    public Identifier getFrame() {
        return new Identifier(frame);
    }

    public double getRotation() {
        return rotation;
    }

    public Identifier getMaterial() {
        return new Identifier(material);
    }

    public int getEntityId() {
        return entityId;
    }

    public Direction getFacing() {
        return facing;
    }

    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }
}
