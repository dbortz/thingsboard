/**
 * Copyright © 2016-2024 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.sql.mobile;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.id.MobileAppId;
import org.thingsboard.server.common.data.id.OAuth2ClientId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.mobile.MobileApp;
import org.thingsboard.server.common.data.mobile.MobileAppOauth2Client;
import org.thingsboard.server.dao.DaoUtil;
import org.thingsboard.server.dao.mobile.MobileAppDao;
import org.thingsboard.server.dao.model.sql.MobileAppEntity;
import org.thingsboard.server.dao.model.sql.MobileAppOauth2ClientCompositeKey;
import org.thingsboard.server.dao.model.sql.MobileAppOauth2ClientEntity;
import org.thingsboard.server.dao.sql.JpaAbstractDao;
import org.thingsboard.server.dao.util.SqlDao;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@SqlDao
public class JpaMobileAppDao extends JpaAbstractDao<MobileAppEntity, MobileApp> implements MobileAppDao {

    private final MobileAppRepository repository;
    private final MobileAppOauth2ClientRepository mobileOauth2ProviderRepository;

    @Override
    protected Class<MobileAppEntity> getEntityClass() {
        return MobileAppEntity.class;
    }

    @Override
    protected JpaRepository<MobileAppEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    public List<MobileApp> findByTenantId(TenantId tenantId) {
        return DaoUtil.convertDataList(repository.findByTenantId(tenantId.getId()));
    }

    @Override
    public List<MobileAppOauth2Client> findOauth2ClientsByMobileAppId(TenantId tenantId, MobileAppId mobileAppId) {
        return  DaoUtil.convertDataList(mobileOauth2ProviderRepository.findAllByMobileAppId(mobileAppId.getId()));
    }

    @Override
    public void saveOauth2Clients(MobileAppOauth2Client mobileAppOauth2Client) {
        mobileOauth2ProviderRepository.save(new MobileAppOauth2ClientEntity(mobileAppOauth2Client));
    }

    @Override
    public void removeOauth2Clients(MobileAppId mobileAppId, OAuth2ClientId oAuth2ClientId) {
        mobileOauth2ProviderRepository.deleteById(new MobileAppOauth2ClientCompositeKey(mobileAppId.getId(), oAuth2ClientId.getId()));
    }

    @Override
    public void deleteByTenantId(TenantId tenantId) {
        repository.deleteByTenantId(tenantId.getId());
    }

}

