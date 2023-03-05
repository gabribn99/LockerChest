package plugin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChestLockerBean {
    private String playerName;
    private String worldName;
    private double x;
    private double y;
    private double z;
}
