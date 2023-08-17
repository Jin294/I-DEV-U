package mate.repository.video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mate.domain.video.VideoRoom;

@Repository
public interface VideoRepository extends JpaRepository<VideoRoom, Integer> {
	VideoRoom findVideoRoomByIdx(int idx);

	VideoRoom findVideoRoomByOvSession(String ovSession);
}
