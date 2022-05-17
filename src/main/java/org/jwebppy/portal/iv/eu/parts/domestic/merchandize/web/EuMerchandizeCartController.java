package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.web;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeCartDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;


@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH  + "/merchandize/cart")
public class EuMerchandizeCartController extends EuMerchandizeGeneralController
{
	@Autowired
	private EuMerchandizeCartService cartService;
	
	@Autowired
	private EuMerchandizeGeneralService merchandizeGeneralService;

	@Autowired
	private Environment environment;
	
		
	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{	
		model.addAttribute("userCorp", getErpUserInfo().getCorpName());
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	// 장바구니 n건 조회
	@RequestMapping("/list")
	@ResponseBody
	public Object list(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap sapParamMap = getErpUserInfo();
		
		MerchandizeCartDto cartDto = new MerchandizeCartDto();
		cartDto.setCorp(sapParamMap.getCorpName());
		cartDto.setRegUsername(sapParamMap.getUsername());
		cartDto.setProductImagePath(environment.getProperty("file.upload.rootPath"));
		List<MerchandizeCartDto> cartList = cartService.getCartItems(cartDto);
		
		if (CmStringUtils.isNotEmpty(cartList)) 
		{
			// SAP 연동: 가격정보 취득
			RfcResponse rfcResponse = merchandizeGeneralService.getProductInfoBySap(sapParamMap);
			int loopEndCnt = 0;
			DataList sapProductList = rfcResponse.getTable("GT_ZSSS0101");
			for (int i = 0, size = sapProductList.size(); i < size; i++)
			{
				if (loopEndCnt == cartList.size())
				{
					break;
				}
				
				DataMap dataMap = (DataMap)sapProductList.get(i);
				
				for (MerchandizeCartDto cDto : cartList)
				{
					if (cDto.getMaterialNo().equals(dataMap.getString("MATNR")))					
					{
						double unitPrice = CmNumberUtils.toDouble(dataMap.getString("KBETR"));
						cDto.setOrderPrice((int)unitPrice);
						if (!EuMerchandizeCommonVo.CURRENCY_KRW.equals(dataMap.getString("KONWA")))
						{
							cDto.setOrderTotalPrice((int)(Math.round(unitPrice * cDto.getOrderQty() / EuMerchandizeCommonVo.round_digit_2))); // 소수점 2번째 자리까지 계산
						} else 
						{
							cDto.setOrderTotalPrice((int)(Math.round(unitPrice * cDto.getOrderQty()))); // 한국 원 단위는 소수점 없음
						}
						
						loopEndCnt++;
						break;
					}
				}			
			}
		}
		
		return cartList;
	}

	// 장바구니 등록 또는 수정
	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute MerchandizeCartDto cartDto) 
	{
		ErpDataMap paramMap = getErpUserInfo();
		cartDto.setCorp(paramMap.getCorpName());
		
		int resultCnt = cartService.saveCart(cartDto);
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);
		return resultMap;		
	}	
	
	// 장바구니 삭제	
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "mcSeq", required = false) List<Integer> mcSeqs, @RequestParam(value = "mpSeq", required = false) List<Integer> mpSeqs) 
	{
		int resultCnt = cartService.deleteCartItem(mcSeqs, mpSeqs, getErpUserInfo());
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);		
		return resultMap;
	}
	

	// 장바구니 추가
	@PostMapping("/cart")
	@ResponseBody
	public Object cart(@RequestParam(value = "mpSeq", required = false) List<Integer> mpSeqs, @RequestParam(value = "quantity", required = false) List<Integer> quantities) 
	{
		int resultCnt = cartService.mergeCartItem(mpSeqs, quantities, getErpUserInfo());
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);		
		return resultMap;
	}
}
