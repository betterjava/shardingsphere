/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.api.strategy.slave;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡策略.
 *
 * @author zhangliang
 */
public final class RoundRobinSlaveLoadBalanceStrategy implements SlaveLoadBalanceStrategy {
    
    private static final ConcurrentHashMap<String, AtomicInteger> COUNT_MAP = new ConcurrentHashMap<>();
    
    @Override
    public String getDataSource(final String logicDataSource, final List<String> slaveDataSources) {
        AtomicInteger count = COUNT_MAP.containsKey(logicDataSource) ? COUNT_MAP.get(logicDataSource) : new AtomicInteger(0);
        COUNT_MAP.putIfAbsent(logicDataSource, count);
        if (count.get() >= slaveDataSources.size()) {
            count.set(0);
        }
        return slaveDataSources.get(count.getAndIncrement() % slaveDataSources.size());
    }
}
