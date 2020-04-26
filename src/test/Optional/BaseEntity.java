package test.Optional;

import java.io.Serializable;
import java.util.Optional;

public class BaseEntity implements Serializable {
    protected Optional<Long> id = Optional.ofNullable(null);


    public Long getId() {
        if (id.isPresent()) {
            return id.get();
        } else
            return null;
    }

    public void setId(Long id) {
        try {
            this.id = Optional.of(id);
        } catch (Exception e) {
            System.out.println("Id is null!");
        }

    }
}
