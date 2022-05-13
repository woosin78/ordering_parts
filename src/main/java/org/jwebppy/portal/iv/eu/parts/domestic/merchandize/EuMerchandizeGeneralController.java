package org.jwebppy.portal.iv.eu.parts.domestic.merchandize;

import java.io.File;
import java.util.List;

import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.jwebppy.portal.iv.eu.parts.common.web.EuPartsGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeCategoryDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/merchandize/general")
public class EuMerchandizeGeneralController extends EuPartsGeneralController
{
	//@Autowired
	//private EuMerchandizeGeneralService merchandizeGeneralService;
	
	@Autowired
	private EuMerchandizeCategoryService categoryService;	

	@Autowired
	private Environment environment;
	

	// 정렬 순서 변경
	/*
	@PostMapping("/changeSortNumber")
	@ResponseBody
	public Object changeSortNumber(@ModelAttribute MerchandizeGeneralDto generalDto) 
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		generalDto.setCorp(paramMap.getCorpName());
		int resultCnt = merchandizeGeneralService.changeSortNumber(generalDto);
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);
		return resultMap;		
	}	
	*/
	
	
	// Setting Category Select Box Info
	protected void setCategorySelectCode(Model model) 
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		MerchandizeCategoryDto categoryDto = new MerchandizeCategoryDto();
		categoryDto.setCorp(paramMap.getCorpName());
		categoryDto.setCategoryLang(paramMap.getLang());	
		List<MerchandizeCategoryDto> catList = categoryService.getCategorys(categoryDto);
		model.addAttribute("categoryList", catList);
	}
	
	// /ordering/src/main/java/com/doosan/iv/portal/corp/eu/scm/ScmGeneralController.java 필요부분만 복사
	protected boolean isPartsManager()
	{
		if (UserAuthenticationUtils.hasRole("EU_SS_ADMIN") || UserAuthenticationUtils.hasRole("UK_SS_ADMIN"))
		{
			return true;
		}

		return false;
	}
	
	
	// 지원 언어 정보 취득
	protected void setSupprotLangs(Model model) 
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		LangKindEntity langKindEntity = new LangKindEntity();
		langKindEntity.setCorp(paramMap.getCorpName());
		List<LangKindEntity> langKindEntityList = categoryService.getSupportLangs(langKindEntity);
		model.addAttribute("langKindEntityList", langKindEntityList);
	}
	

	// 파일 경로 반환
	protected String getMerchandizeFileUploadPath()
	{
		File path = new File(environment.getProperty("file.upload.rootPath") + "mall" + File.separator);

		if (!path.exists())
		{
			path.mkdir();
		}
		return path.getAbsolutePath();
	}
	
}
