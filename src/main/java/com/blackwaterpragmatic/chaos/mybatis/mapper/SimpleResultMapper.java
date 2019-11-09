package com.blackwaterpragmatic.chaos.mybatis.mapper;

import com.blackwaterpragmatic.chaos.bean.Result;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SimpleResultMapper {

	List<Result> list();

}
