package com.amazon.ata.inmemorycaching.classroom.dao;

import com.amazon.ata.inmemorycaching.classroom.dao.models.GroupMembershipCacheKey;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class GroupMembershipCachingDao {
    private final LoadingCache<GroupMembershipCacheKey, Boolean> cache;

    @Inject
    public GroupMembershipCachingDao(final GroupMembershipDao delegateDao) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(20000)
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build(CacheLoader.from(delegateDao::isUserInGroup));
    }

    public boolean isUserInGroup(String userId, String groupId) {
        return cache.getUnchecked(new GroupMembershipCacheKey(userId, groupId));
    }

}
