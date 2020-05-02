package Group1.FaceReco.repository;

import org.springframework.data.repository.CrudRepository;

import Group1.FaceReco.domain.Photo;

public interface SignatureRepository extends CrudRepository<Photo,Long>{

}
