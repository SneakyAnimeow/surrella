package club.anims.surrella.database.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "blacklisted_users", schema = "main")
@NamedQueries({
        @NamedQuery(name = "BlacklistedUser.findByDiscordId", query = "select b from BlacklistedUser b where b.discordId = :discordId"),
        @NamedQuery(name = "BlacklistedUser.existsByDiscordId", query = "select (count(b) > 0) from BlacklistedUser b where b.discordId = :discordId"),
        @NamedQuery(name = "BlacklistedUser.deleteByDiscordId", query = "delete from BlacklistedUser b where b.discordId = :discordId")
})
@NoArgsConstructor
public class BlacklistedUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = true)
    private Integer id;

    @Basic
    @Column(name = "discord_id", nullable = false, length = -1)
    private String discordId;

    public BlacklistedUser(String discordId) {
        this.discordId = discordId;
    }

    public Integer getId() {
        return id;
    }

    public BlacklistedUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDiscordId() {
        return discordId;
    }

    public BlacklistedUser setDiscordId(String discordId) {
        this.discordId = discordId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistedUser that = (BlacklistedUser) o;
        return Objects.equals(id, that.id) && Objects.equals(discordId, that.discordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discordId);
    }
}
