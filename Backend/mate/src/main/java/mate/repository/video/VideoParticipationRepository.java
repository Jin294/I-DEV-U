package mate.repository.video;

import java.util.List;
import java.util.Optional;

import mate.domain.video.VideoRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mate.domain.video.VideoParticipation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VideoParticipationRepository extends JpaRepository<VideoParticipation, Integer> {
    @Query(value = "select vm from VideoRoom as vm join VideoParticipation as vp on vp.videoRoom.idx = vm.idx " +
            "where vp.user.idx = :userIdx")
    List<VideoRoom> findRoomIdxByUserIdx(@Param(value = "userIdx") int userIdx);

    @Transactional
    @Modifying
    @Query(value = "delete from VideoParticipation where videoRoom.idx = :roomIdx and user.idx = :userIdx")
    void deleteByUserIdxAndRoomIdx(@Param(value = "roomIdx") int roomIdx, @Param(value = "userIdx") int userIdx);
}
