package cn.endorphin.atevent.repository.base;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.endorphin.atevent.entity.Alarm;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface AlarmCustomizedRepository
    extends CrudRepository<Alarm, String>, PagingAndSortingRepository<Alarm, String>, cn.endorphin.atevent.repository.AlarmCustomizedRepository {

    Optional<Alarm> findByDedupeKey(String dedupeKey);
}
