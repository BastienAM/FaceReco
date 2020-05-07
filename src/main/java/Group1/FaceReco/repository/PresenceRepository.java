package Group1.FaceReco.repository;

import org.springframework.data.repository.CrudRepository;

import Group1.FaceReco.domain.Presence;
import Group1.FaceReco.domain.PresenceId;

public interface PresenceRepository extends CrudRepository<Presence, PresenceId>{

}
