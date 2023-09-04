package endorphins.april.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import endorphins.april.entity.Alarm;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface AlarmRepository
    extends CrudRepository<Alarm, String>, PagingAndSortingRepository<Alarm, String>, CustomizedAlarmRepository {

    Optional<Alarm> findByDedupeKey(String dedupeKey);
}
