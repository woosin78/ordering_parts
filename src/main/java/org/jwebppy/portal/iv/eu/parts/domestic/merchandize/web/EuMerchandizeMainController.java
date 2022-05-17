package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadInfoDto;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileUploadEntity;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadInfoService;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadService;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralService;
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
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH  + "/merchandize/main")
public class EuMerchandizeMainController extends EuMerchandizeGeneralController
{
	@Autowired
	private EuFileUploadService fileUploadService;

	@Autowired
	private EuFileUploadInfoService fileUploadInfoService;

	@Autowired
	private EuMerchandizeRecommendProductService recommendProductService;

	@Autowired
	private EuMerchandizeGeneralService merchandizeGeneralService;

	@Autowired
	private Environment environment;
	
	
	// 머천다이즈 팝업 URL을 관리자 여부에 따라서 다르게 반환한다.
	@PostMapping("/adminCheck")
	@ResponseBody
	public Object adminCheck(@ModelAttribute MerchandizeProductSearchDto productSearchDto)
	{
		String mallUserUrl = EuMerchandizeCommonVo.MERCHANDIZE_USER_PATH;		
		if (isPartsManager()) 
		{
			mallUserUrl += "_admin";
		}
		
		return mallUserUrl;
	}
	
	
	// 관리자 메인페이지
	@RequestMapping("/main_admin")
	public String mainAdmin(Model model, @RequestParam(value = "fuSeq", required = false) Integer fuSeq) throws Exception
	{
		if (!isPartsManager())
		{
			throw new AccessDeniedException("No permission");
		}
		
		ErpDataMap paramMap = getErpUserInfo();

		// 1.메인 이미지 파일 정보 가져오기
		FileUploadDto fileDto = new FileUploadDto();
		fileDto.setTSeq(0);
		fileDto.setType(EuMerchandizeCommonVo.FILE_TYPE_MAIN);
		List<FileUploadDto> fileUploads= fileUploadService.getFileUploadList(fileDto);
		if (fileUploads!=null) {
			model.addAttribute("fileUploads", fileUploads.get(0));
		}
		FileUploadInfoDto fileUploadInfo = fileUploadInfoService.getFileUploadInfo(EuMerchandizeCommonVo.FILECHECK_MALL_MAIN_ID);
		model.addAttribute("fileUploadInfo", fileUploadInfo);

		// 2. 추천상품 리스트 가져오기
		MerchandizeRecommendProductDto dto = new MerchandizeRecommendProductDto();
		dto.setProductImagePath(environment.getProperty("file.upload.rootPath"));
		dto.setCorp(paramMap.getCorpName());
		List<MerchandizeRecommendProductDto> recommandDtos = recommendProductService.getRecommendProductItemsMain(dto);
		if (CmStringUtils.isNotEmpty(recommandDtos))
		{
			paramMap.put("recommandDtos", recommandDtos);
			// SAP 연동: 가격정보 취득
			RfcResponse rfcResponse = merchandizeGeneralService.getProductInfoBySap(paramMap);
			int loopEndCnt = 0;
			DataList sapProductList = rfcResponse.getTable("GT_ZSSS0101");
			for (int i = 0, size = sapProductList.size(); i < size; i++)
			{
				if (loopEndCnt == recommandDtos.size())
				{
					break;
				}

				DataMap dataMap = (DataMap)sapProductList.get(i);
				for (MerchandizeRecommendProductDto productDto : recommandDtos)
				{
					if (productDto.getMaterialNo().equals(dataMap.getString(EuMerchandizeCommonVo.SAP_FIELD_MATNR)))
					{
						productDto.setPrice((int)CmNumberUtils.toDouble(dataMap.getString(EuMerchandizeCommonVo.SAP_FIELD_KBETR), 0));
						productDto.setCurrency(dataMap.getString(EuMerchandizeCommonVo.SAP_FIELD_KONWA));
						loopEndCnt++;
						break;
					}
				}
			}

			model.addAttribute("productList", recommandDtos);
			model.addAttribute("recommendListSize", recommandDtos.size());
		} else
		{
			model.addAttribute("recommendListSize", 0);
		}

		// 3. 추천상품 리스트 정렬 옵션 정보 가져오기(유효한 옵션값만 만들기 위함)
		MerchandizeRecommendProductDto recommandDto = recommendProductService.getRecommendProductItemsMainCount(dto);
		model.addAttribute("recommendSortStrings", recommandDto.getSortStrings());
		model.addAttribute("recommendSortMin", recommandDto.getSortMin());
		model.addAttribute("recommendSortMax", recommandDto.getSortMax());

		model.addAttribute("fileValidStr", EuMerchandizeCommonVo.FILECHECK_MALL_MAIN_ID);

		return DEFAULT_VIEW_URL;
	}


	// 관리자 메인이미지 팝업 등록 화면 호출
	@RequestMapping("/main_fileupload")
	public String mainFileuploadPop(Model model, WebRequest webRequest)
	{
		model.addAttribute("fileValidStr", EuMerchandizeCommonVo.FILECHECK_MALL_MAIN_ID);
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}
	
	
	@PostMapping("/save_main_file")
	@ResponseBody
	public Object saveMainFile(@ModelAttribute("fileUpload") FileUploadDto inFileUpload) throws Exception
    {	
		String imagepathStr = "";		
		ErpDataMap paramMap = getErpUserInfo();
		
		if (isPartsManager())
		{
			FileUploadDto fileUploadDto = fileUploadService.checkFileInfo(inFileUpload, EuMerchandizeCommonVo.FILECHECK_MALL_MAIN_ID);
			if (CmStringUtils.isEmpty(inFileUpload.getType()))
			{
				inFileUpload.setType(EuMerchandizeCommonVo.FILE_TYPE_MAIN);
			}
			inFileUpload.setTSeq(0);	// 메인 화면 대문 이미지 고정 시퀀스 수치
			inFileUpload.setFileStoragePath(fileUploadDto.getFileStoragePath());
			inFileUpload.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
			inFileUpload.setBbsCorp(paramMap.getCorpName());
			inFileUpload.setUserCorp(paramMap.getCorpName());
			inFileUpload.setModUsername(paramMap.getUsername());
			inFileUpload.setModDate(LocalDateTime.now());
			
			// 기존 메인 파일 논리 삭제처리(20200325 : 물리 파일 삭제는 정식 서비스 이후에도 필요없다고 전닯받아 삭제로직 생략(공통 파일 업로드 서비스에도 주석처리됨))
			FileUploadEntity fuSeqEntity = fileUploadService.getFuSeq(inFileUpload);
			if (CmStringUtils.isNotEmpty(fuSeqEntity)) {
				inFileUpload.setFuSeq(fuSeqEntity.getFuSeq());   
	            fileUploadService.fileDeleteMallMain(inFileUpload);
			}
                        
			FileUploadDto rtnFileUpload = new FileUploadDto();
			List<String> fgFileStatuss = inFileUpload.getFgFileStatuss();
			for (int i = 0; i < fgFileStatuss.size(); i++) 
			{
				if ("I".equals(fgFileStatuss.get(i))) 
				{
					rtnFileUpload = fileUploadService.fileTransfer(inFileUpload.getUserFiles().get(i), inFileUpload.getFileStoragePath());
					rtnFileUpload.setTSeq(inFileUpload.getTSeq());
					rtnFileUpload.setTarget(inFileUpload.getTarget());
					rtnFileUpload.setType(inFileUpload.getType());
					fileUploadService.insertFileUpload(rtnFileUpload);	// return 값으로 현재 insert된 파일읠 fu_seq 값을 얻을 수 있으나, 파일 저장 이후 화면 처리 방식을 form submit에서 ajax 후속처리로 변경함에 따라 반환값이 필요없어지게 됨 
				}
			}
			
			imagepathStr = environment.getProperty("file.upload.rootPath") + rtnFileUpload.getStoragePath() + rtnFileUpload.getSavedName();
		} else
		{
			throw new AccessDeniedException("No permission");
		}
		
		return imagepathStr;
    }


	// 사용자 메인페이지
	@RequestMapping("/main")
	public String main(Model model)
	{
		ErpDataMap paramMap = getErpUserInfo();

		MerchandizeRecommendProductDto reProdDto = new MerchandizeRecommendProductDto();
		reProdDto.setCorp(paramMap.getCorpName());
		MerchandizeRecommendProductDto currentCntDto = recommendProductService.checkRecommendProductItem(reProdDto);
		model.addAttribute("currentCount", currentCntDto.getTotalCount());

		setCategorySelectCode(model);

		// 추천상품 리스트
		MerchandizeRecommendProductDto dto = new MerchandizeRecommendProductDto();
		dto.setCorp(paramMap.getCorpName());
		dto.setProductImagePath(environment.getProperty("file.upload.rootPath"));
		List<MerchandizeRecommendProductDto> recommandDtos = recommendProductService.getRecommendProductItemsMain(dto);
		if (CmStringUtils.isNotEmpty(recommandDtos))
		{
			paramMap.put("recommandDtos", recommandDtos);
			// SAP 연동: 가격정보 취득
			RfcResponse rfcResponse = merchandizeGeneralService.getProductInfoBySap(paramMap);
			int loopEndCnt = 0;
			DataList sapProductList = rfcResponse.getTable("GT_ZSSS0101");
			for (int i = 0, size = sapProductList.size(); i < size; i++)
			{
				if (loopEndCnt == recommandDtos.size())
				{
					break;
				}

				DataMap dataMap = (DataMap)sapProductList.get(i);

				for (MerchandizeRecommendProductDto productDto : recommandDtos)
				{
					if (productDto.getMaterialNo().equals(dataMap.getString(EuMerchandizeCommonVo.SAP_FIELD_MATNR)))
					{
						productDto.setPrice((int)CmNumberUtils.toDouble(dataMap.getString(EuMerchandizeCommonVo.SAP_FIELD_KBETR), 0));
						productDto.setCurrency(dataMap.getString(EuMerchandizeCommonVo.SAP_FIELD_KONWA));
						loopEndCnt++;
						break;
					}
				}
			}

			model.addAttribute("productList", recommandDtos);
			model.addAttribute("recommendListSize", recommandDtos.size());
		} else
		{
			model.addAttribute("recommendListSize", 0);
		}

		model.addAttribute("userCorp", getErpUserInfo().getCorpName());	// [바로 주문]버튼이 있는 페이지에서 어떤 법인의 주문 화면으로 이동할지 알려주기 위한 처리

		return DEFAULT_VIEW_URL;
	}


	// 추천상품 n건 조회
	@PostMapping("/list_admin")
	@ResponseBody
	public Object listAdmin(@ModelAttribute MerchandizeProductSearchDto productSearchDto)
	{
		ErpDataMap paramMap = getErpUserInfo();

		productSearchDto.setCorp(paramMap.getCorpName());
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
	public Object select(@RequestParam(value = "mrpSeq", required = false) List<Integer> mrpSeqs)
	{
		int resultCnt = recommendProductService.insertRecommendProducts(mrpSeqs);
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
