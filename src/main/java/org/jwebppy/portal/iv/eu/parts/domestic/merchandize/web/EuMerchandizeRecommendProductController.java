package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductSearchDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeRecommendProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeRecommendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH  + "/merchandize/recommendproduct")
public class EuMerchandizeRecommendProductController extends EuMerchandizeGeneralController
{
	@Autowired
	private EuMerchandizeRecommendProductService recommendProductService;

	@Autowired
	private Environment environment;
	
		
	@RequestMapping("/main_admin")
	public String mainAdmin(Model model, WebRequest webRequest)
	{
		if (!isPartsManager())
		{
			throw new AccessDeniedException("No permission");
		}
		
		setCategorySelectCode(model);
				
		MerchandizeRecommendProductDto currentCntDto = recommendProductService.checkRecommendProductItem(new MerchandizeRecommendProductDto());
		model.addAttribute("currentCount", currentCntDto.getTotalCount());
		model.addAttribute("currentLimitCount", EuMerchandizeCommonVo.REPRODUCT_MALL_ITEM_LIMIT);	// 현재 설정된 최대 추천상품 갯수
		
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
		
	}
		
	// 추천상품 n건 조회
	@PostMapping("/list_admin")
	@ResponseBody
	public Object listAdmin(@ModelAttribute MerchandizeProductSearchDto productSearchDto) 
	{	
		if (!isPartsManager())
		{
			throw new AccessDeniedException("No permission");
		}
		
		ErpDataMap paramMap = getErpUserInfo();		
		productSearchDto.setCorp(paramMap.getCorpName());
		
		productSearchDto.setProductImagePath( environment.getProperty("file.upload.rootPath") );	// 이미지 파일 패스를 추가하여 가져오기 위함
		
		List<MerchandizeProductDto> productResultDtoList = recommendProductService.getRecommendProductItems(productSearchDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("productList", productResultDtoList);
		if (productResultDtoList != null)
		{
			resultMap.put("resCnt", productResultDtoList.size());
		} else 
		{
			resultMap.put("resCnt", 0);
		}		
		return resultMap;		
	}	
	
	
	// 추천상품 등록 또는 수정
	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute MerchandizeRecommendProductDto reProductDto) 
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		reProductDto.setCorp(paramMap.getCorpName());
		int resultCnt = recommendProductService.saveRecommendProduct(reProductDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);
		return resultCnt;	
	}	


	// 추천상품 선택	
	@PostMapping("/select")
	@ResponseBody
	public Object select(@RequestParam(value = "mpSeq", required = false) List<Integer> mpSeqs)
	{
		int resultCnt = recommendProductService.insertRecommendProducts(mpSeqs);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);
		
		return resultMap;
	}	

	
	// 추천상품 삭제	
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "mrpSeq", required = false) List<Integer> mrpSeqs)
	{
		int resultCnt = recommendProductService.deleteRecommendProducts(mrpSeqs);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);
		
		return resultMap;
	}	

}
