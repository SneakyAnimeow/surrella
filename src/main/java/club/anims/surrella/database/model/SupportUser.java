package club.anims.surrella.database.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "support_users", schema = "main")
@NamedQueries({
        @NamedQuery(name = "SupportUser.findByDiscordId", query = "select s from SupportUser s where s.discordId = :discordId"),
        @NamedQuery(name = "SupportUser.deleteByDiscordId", query = "delete from SupportUser s where s.discordId = :discordId"),
        @NamedQuery(name = "SupportUser.existsByDiscordId", query = "select (count(s) > 0) from SupportUser s where s.discordId = :discordId")
})
@NoArgsConstructor
public class SupportUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = true)
    private Integer id;
    @Basic
    @Column(name = "discord_id", nullable = false, length = -1)
    private String discordId;

    public SupportUser(String discordId) {
        this.discordId = discordId;
    }

    public Integer getId() {
        return id;
    }

    public SupportUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDiscordId() {
        return discordId;
    }

    public SupportUser setDiscordId(String discordId) {
        this.discordId = discordId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportUser that = (SupportUser) o;
        return Objects.equals(id, that.id) && Objects.equals(discordId, that.discordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discordId);
    }
}
