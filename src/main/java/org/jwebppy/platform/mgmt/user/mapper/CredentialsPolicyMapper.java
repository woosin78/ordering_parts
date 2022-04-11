package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
import org.jwebppy.platform.mgmt.user.entity.CredentialsPolicyEntity;

@NoLogging
@Mapper
public interface CredentialsPolicyMapper
{
	public int insert(CredentialsPolicyEntity credentialsPolicy);
	public int update(CredentialsPolicyEntity credentialsPolicy);
	public int updateFgDelete(Integer cpSeq);
	public CredentialsPolicyEntity findCredentialsPolicy(Integer cpSeq);
	public List<CredentialsPolicyEntity> findCredentialsPolicies(CredentialsPolicySearchDto credentialsPolicySearch);
	public List<CredentialsPolicyEntity> findPageCredentialsPolicies(CredentialsPolicySearchDto credentialsPolicySearch);
}
