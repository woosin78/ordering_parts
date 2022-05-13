package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.web;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeCategoryDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeLangRlDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeLangRlEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeCategoryService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeLangRlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/merchandize/category")
public class EuMerchandizeCategoryController extends EuMerchandizeGeneralController
{
	@Autowired
	private EuMerchandizeCategoryService categoryService;

	@Autowired
	private EuMerchandizeLangRlService langService;

	@Autowired
	private EuMerchandizeGeneralService merchandizeGeneralService;

	
	@RequestMapping("/main")
	public String main()
	{
		//
		return DEFAULT_VIEW_URL;
	}
	
	
	@RequestMapping("/main_admin")
	public String mainAdmin(Model model)
	{
		if (!isPartsManager())
		{
			throw new AccessDeniedException("No permission");
		}
		
		ErpDataMap paramMap = getErpUserInfo();
		
		MerchandizeCategoryDto categoryDto = new MerchandizeCategoryDto();
		categoryDto.setCorp(paramMap.getCorpName());
		categoryDto.setCategoryLang(paramMap.getLang());
		List<MerchandizeCategoryDto> catList = categoryService.getCategorys(categoryDto);

		model.addAttribute("categoryList", catList);
		model.addAttribute("categoryFormat", "dd.MM.yyyy");
		model.addAttribute("categoryListTotalCnt", catList == null? 0 : catList.size());

		MerchandizeCategoryDto sortCountDto = categoryService.getCategoryItemsMainCount(categoryDto);
		model.addAttribute("categorySortStrings", sortCountDto.getSortStrings());

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/register")
	public String register(Model model)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		MerchandizeCategoryDto categoryDto = new MerchandizeCategoryDto();
		categoryDto.setCorp(paramMap.getCorpName());
		categoryDto.setCategoryLang(paramMap.getLang());
		MerchandizeCategoryDto resultDto = categoryService.getCategoryNewSortNumber(categoryDto);
		model.addAttribute("categoryDto", resultDto);

		LangKindEntity langKindEntity = new LangKindEntity();
		if (CmStringUtils.isEmpty(langKindEntity.getCorp()))
		{
			langKindEntity.setCorp(paramMap.getCorpName());
		}
		List<LangKindEntity> langKindEntityList = categoryService.getSupportLangs(langKindEntity);
		model.addAttribute("langKindEntityList", langKindEntityList);

		// 현재 등록된 카테고리 정보를 취득
		List<MerchandizeCategoryDto> currCatInfDtos = categoryService.getCategorys(categoryDto);
		
		// SAP에서 관리중인 카테고리 정보를 모두 가져와서, 현재 등록되어 있는 카테고리 정보를 제외함(미등록된 카테고리만 표시하기 위함)
		RfcResponse rfcResponse = merchandizeGeneralService.getCategoryInfoBySap();
		DataList sapCategoryList = rfcResponse.getTable("LT_ZSSS0100");
		
		// 현재 유지하고 있는 카테고리 리스트가 하나 이상 있을 경우, SAP Category List와 겹치는 카테고리 코드를 삭제
		if (CmStringUtils.isNotEmpty(currCatInfDtos))
		{
			int[] removeIntArr = new int[sapCategoryList.size()];
			int removeSize = 0;
			for (int i = 0, size = sapCategoryList.size(); i < size; i++)
			{
				DataMap dataMap = (DataMap)sapCategoryList.get(i);
				
				for (MerchandizeCategoryDto dto : currCatInfDtos)
				{
					if (dto.getCategoryCode().equals(dataMap.getString("PRODH")))	
					{
						removeIntArr[removeSize] = i;
						removeSize++;
						break;
					}
				}			
			}
			// 현재 등록되어 있는 카테고리 코드와 겹치는 SAP Category코드를 제거
			for (int j = removeSize - 1; j >= 0; j--)
			{
				sapCategoryList.remove(removeIntArr[j]);
			}
		}
		
		model.addAttribute("sapCategoryList", sapCategoryList);		
		return DEFAULT_VIEW_URL;
	}


	// 카테고리 n건 조회
	@RequestMapping("/list")
	@ResponseBody
	public Object list(@RequestParam Map<String, Object> paramMap)
	{
		if (!isPartsManager())
		{
			throw new AccessDeniedException("No permission");
		}
		
		ErpDataMap paramDataMap = getErpUserInfo();
		
		MerchandizeCategoryDto categoryDto = new MerchandizeCategoryDto();
		categoryDto.setCorp(paramDataMap.getCorpName());	
		categoryDto.setCategoryLang(paramDataMap.getLang());	
		List<MerchandizeCategoryDto> categoryResultDtos = categoryService.getCategorys(categoryDto);
		return categoryResultDtos;
	}
	

	// 카테고리 1건 조회
	@RequestMapping("/view")
	public String view(Model model, @ModelAttribute MerchandizeCategoryDto categoryDto)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		categoryDto.setCorp(paramMap.getCorpName());
		MerchandizeCategoryDto categoryResultDto = categoryService.getCategory(categoryDto);

		MerchandizeLangRlEntity langEntity = new MerchandizeLangRlEntity();
		langEntity.setTSeq(categoryDto.getMcSeq());
		langEntity.setCorp(paramMap.getCorpName());
		langEntity.setType(EuMerchandizeCommonVo.CATEGORY_NAME);
		List<MerchandizeLangRlDto> categoryLangDtos = langService.getSupportLangsModify(langEntity);

		model.addAttribute("categoryDto", categoryResultDto);
		model.addAttribute("categoryLangDtos", categoryLangDtos);

		return DEFAULT_VIEW_URL;
	}

	
	// 카테고리 1건 수정
	@RequestMapping("/modify")
	public String modify(Model model, @ModelAttribute MerchandizeCategoryDto categoryDto)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		categoryDto.setCorp(paramMap.getCorpName());	
		categoryDto.setCategoryLang(paramMap.getLang());	
		MerchandizeCategoryDto categoryResultDto = categoryService.getCategory(categoryDto);

		MerchandizeLangRlEntity langEntity = new MerchandizeLangRlEntity();
		langEntity.setTSeq(categoryDto.getMcSeq());
		langEntity.setCorp(paramMap.getCorpName());
		langEntity.setType(EuMerchandizeCommonVo.CATEGORY_NAME);
		List<MerchandizeLangRlDto> categoryLangDtos = langService.getSupportLangsModify(langEntity);

		model.addAttribute("categoryDto", categoryResultDto);
		model.addAttribute("categoryLangDtos", categoryLangDtos);

		setSupprotLangs(model);
		
		// SAP에서 현재 미등록되어 있는 카테고리 정보를 가져온다.
		// 현재 등록된 카테고리 정보를 취득
		List<MerchandizeCategoryDto> currCatInfDtos = categoryService.getCategorys(categoryDto);
		// SAP에서 관리중인 카테고리 정보를 모두 가져와서, 현재 등록되어 있는 카테고리 정보를 제외함(미등록된 카테고리만 표시하기 위함)
		RfcResponse rfcResponse = merchandizeGeneralService.getCategoryInfoBySap();
		DataList sapCategoryList = rfcResponse.getTable("LT_ZSSS0100");		
		int[] removeIntArr = new int[sapCategoryList.size()];
		int removeSize = 0;
		for (int i = 0, size = sapCategoryList.size(); i < size; i++)
		{
			DataMap dataMap = (DataMap)sapCategoryList.get(i);
			
			for (MerchandizeCategoryDto dto : currCatInfDtos)
			{
				if (dto.getCategoryCode().equals(dataMap.getString("PRODH")))	
				{
					removeIntArr[removeSize] = i;
					removeSize++;
					break;
				}
			}			
		}
		// 현재 등록되어 있는 카테고리 코드와 겹치는 SAP Category코드를 제거
		for (int j = removeSize - 1; j >= 0; j--)
		{
			sapCategoryList.remove(removeIntArr[j]);
		}		
		model.addAttribute("sapCategoryList", sapCategoryList);		

		return DEFAULT_VIEW_URL;
	}

	
	// 카테고리 등록 또는 수정
	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute MerchandizeCategoryDto categoryDto)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		categoryDto.setCorp(paramMap.getCorpName());
		
		int resultCnt = categoryService.saveCategory(categoryDto);
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);
		return resultMap;
	}

	
	// 카테고리 삭제
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "mcSeq", required = false) List<Integer> mcSeqs)
	{
		int resultCnt = categoryService.deleteCategory(mcSeqs);

		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);

		return resultMap;
	}

}
