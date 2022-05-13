package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.web;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadInfoDto;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadInfoService;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeLangRlDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductSearchDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeLangRlEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeLangRlService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service.EuMerchandizeProductService;
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
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/merchandize/product")
public class MerchandizeProductController extends EuMerchandizeGeneralController
{
	@Autowired
	private EuMerchandizeProductService productService;

	@Autowired
	private EuFileUploadService fileUploadService;

	@Autowired
	private EuFileUploadInfoService fileUploadInfoService;

	@Autowired
	private EuMerchandizeLangRlService langService;

	@Autowired
	private EuMerchandizeGeneralService merchandizeGeneralService;

	@Autowired
	private Environment environment;
	

	@RequestMapping("/main")
	public String main(Model model, MerchandizeProductDto dto)
	{
		setCategorySelectCode(model);

		if (CmStringUtils.isNotEmpty(dto))
		{
			model.addAttribute("selectedMcSeq", dto.getMcSeq());
		}
		
		model.addAttribute("userCorp", getErpUserInfo().getCorpName());
		model.addAttribute("previewLimitCnt", EuMerchandizeCommonVo.PRODUCT_IMAGE_PREVIEW_LIMIT);
		
		
		return DEFAULT_VIEW_URL;
	}
	
	
	@RequestMapping("/main_admin")
	public String mainAdmin(Model model)
	{
		if (!isPartsManager())
		{
			throw new AccessDeniedException("No permission");
		}
		
		setCategorySelectCode(model);
		return DEFAULT_VIEW_URL;
	}

	
	// 상품 n건 조회
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
		productSearchDto.setProductImagePath( environment.getProperty("file.upload.rootPath") );
		
		List<MerchandizeProductDto> productResultDtoList = productService.getProductItems(productSearchDto);
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

	
	// 상품 1건 상세조회
	@RequestMapping("/view")
	public String view(Model model, @ModelAttribute MerchandizeProductDto productDto, @RequestParam(value="mpSeq", required=false) Integer mpSeq)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		// 1. 상품정보 취득
		productDto.setCorp(paramMap.getCorpName());
		if (CmStringUtils.isNotEmpty(mpSeq))
		{
			productDto.setMpSeq(mpSeq);
		}
		MerchandizeProductDto productResultDto = productService.getProduct(productDto);

		// 2. 현재 지원되는 다국어 정보 모음 취득
		MerchandizeLangRlEntity langEntity = new MerchandizeLangRlEntity();
		langEntity.setTSeq(productDto.getMpSeq());
		langEntity.setCorp(paramMap.getCorpName());
		langEntity.setType(EuMerchandizeCommonVo.PRODUCT_NAME);
		List<MerchandizeLangRlDto> productNameLangDtos = langService.getSupportLangsModify(langEntity);

		langEntity.setType(EuMerchandizeCommonVo.PRODUCT_DESCRIPTION);
		List<MerchandizeLangRlDto> productDescLangDtos = langService.getSupportLangsModify(langEntity);

		// 3. 상품에 엮인 이미지 파일 정보 취득
		FileUploadDto fileDto = new FileUploadDto();
		fileDto.setTSeq(mpSeq);
		fileDto.setType(EuMerchandizeCommonVo.FILE_TYPE_IMAGE);
		List<FileUploadDto> fileUploads = fileUploadService.getFileUploadList(fileDto);

		model.addAttribute("productDto", productResultDto);
		model.addAttribute("productNameLangDtos", productNameLangDtos);
		model.addAttribute("productDescLangDtos", productDescLangDtos);
		model.addAttribute("fileUploads", fileUploads);

		return DEFAULT_VIEW_URL;
	}


	// 상품 1건 수정
	@RequestMapping("/modify")
	public String modify(Model model, @ModelAttribute MerchandizeProductDto productDto)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		// 1. 카테고리 코드 Select Box 에 탑재되는 정보 취득
		setCategorySelectCode(model);

		// 2. 현재 지원되는 다국어 정보 모음 취득
		setSupprotLangs(model);

		// 3. 상품정보 취득
		productDto.setCorp(paramMap.getCorpName());
		MerchandizeProductDto productResultDto = productService.getProduct(productDto);

		// 4. 상품 다국어 정보 취득
		MerchandizeLangRlEntity langEntity = new MerchandizeLangRlEntity();
		langEntity.setTSeq(productDto.getMpSeq());
		langEntity.setCorp(paramMap.getCorpName());
		langEntity.setType(EuMerchandizeCommonVo.PRODUCT_NAME);
		List<MerchandizeLangRlDto> productNameLangDtos = langService.getSupportLangsModify(langEntity);

		langEntity.setType(EuMerchandizeCommonVo.PRODUCT_DESCRIPTION);
		List<MerchandizeLangRlDto> productDescLangDtos = langService.getSupportLangsModify(langEntity);

		// 5. 상품에 엮인 이미지 파일 정보 취득
		FileUploadDto fileDto = new FileUploadDto();
		fileDto.setTSeq(productResultDto.getMpSeq());
		fileDto.setType(EuMerchandizeCommonVo.FILE_TYPE_IMAGE);
		List<FileUploadDto> fileUploads = fileUploadService.getFileUploadList(fileDto);

		// 6. 상품 파일 Validation Check 에 사용하는 정보 취득
		FileUploadInfoDto fileUploadInfo = fileUploadInfoService.getFileUploadInfo(EuMerchandizeCommonVo.FILECHECK_MALL_PRODUCT_ID);

		model.addAttribute("productDto", productResultDto);
		model.addAttribute("productNameLangDtos", productNameLangDtos);
		model.addAttribute("productDescLangDtos", productDescLangDtos);
		model.addAttribute("fileUploads", fileUploads);
		model.addAttribute("fileUploadInfo", fileUploadInfo);
		model.addAttribute("fileValidStr", EuMerchandizeCommonVo.FILECHECK_MALL_PRODUCT_ID);

		return DEFAULT_VIEW_URL;
	}
	

	// 상품 등록
	@RequestMapping("/register")
	public String register(Model model)
	{
		// 1. 카테고리 코드 Select Box 에 탑재되는 정보 취득
		setCategorySelectCode(model);
		// 2. 현재 지원되는 다국어 정보 모음 취득
		setSupprotLangs(model);
		// 3. 상품 파일 Validation Check 에 사용하는 정보 취득
		FileUploadInfoDto fileUploadInfo = fileUploadInfoService.getFileUploadInfo(EuMerchandizeCommonVo.FILECHECK_MALL_PRODUCT_ID);
		model.addAttribute("fileUploadInfo", fileUploadInfo);

		model.addAttribute("fileValidStr", EuMerchandizeCommonVo.FILECHECK_MALL_PRODUCT_ID);

		return DEFAULT_VIEW_URL;
	}


	// 상품 등록 내용이나 수정 내용을 저장
	@PostMapping("/save")
	public RedirectView save( @ModelAttribute MerchandizeProductDto productDto
			, @ModelAttribute("fileUpload") FileUploadDto inFileUpload
			, HttpServletResponse response
			) throws Exception
	{	
		ErpDataMap paramMap = getErpUserInfo();
		
		String returnStr = CmStringUtils.isNotEmpty(productDto.getMpSeq()) ? EuMerchandizeCommonVo.UPDATE : EuMerchandizeCommonVo.INSERT;

		// 파일 업로드 공통 Validation 처리 Start
		productDto.setCorp(paramMap.getCorpName());
		inFileUpload.setCorp(paramMap.getCorpName());		
		FileUploadDto fileUpload = fileUploadService.checkFileInfo(inFileUpload, EuMerchandizeCommonVo.FILECHECK_MALL_PRODUCT_ID);

		if (inFileUpload.getTotalFileSize() > fileUpload.getFileLimitSize())
		{
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('파일사이즈 초과'); history.go(-1);</script>");
			out.flush();
		}
		if (fileUpload.getFileNames().size() < 0)
		{
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('File Extension Error'); history.go(-1);</script>");
			out.flush();
		}
		// 파일 업로드 공통 Validation 처리 End

		inFileUpload.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
		inFileUpload.setTSeq(productDto.getMpSeq());		
		inFileUpload.setFileStoragePath(getMerchandizeFileUploadPath());		
		inFileUpload.setType(EuMerchandizeCommonVo.FILE_TYPE_IMAGE);

		productService.saveProduct(productDto, inFileUpload);

		if (EuMerchandizeCommonVo.INSERT.equals(returnStr))
		{
			return new RedirectView("/portal/corp/eu/scm/parts/merchandize/product/main_admin");
		} else
		{
			return new RedirectView("/portal/corp/eu/scm/parts/merchandize/product/view?mpSeq=" + productDto.getMpSeq());
		}
	}


	// 상품 삭제
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "mpSeq", required = false) List<Integer> mpSeqs)
	{
		int resultCnt = productService.deleteProduct(mpSeqs);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCnt", resultCnt);

		return resultMap;
	}
	
	
	// 상품코드 유효성 SAP체크(카테고리 코드 유효성도 같이 체크)
	@PostMapping("/prdCdValidChk")
	@ResponseBody
	public Object prdCdValidChk( @ModelAttribute MerchandizeProductDto productDto)
	{	
		ErpDataMap paramMap = getErpUserInfo();
		String resultMsgCd = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
			
		paramMap.put("materialNo", productDto.getMaterialNo());
		RfcResponse rfcResponse = merchandizeGeneralService.checkValidProductBySap(paramMap);				
		DataList sapProductList = rfcResponse.getTable("LT_ITEM");
		for (int i = 0, size = sapProductList.size(); i < size; i++)
		{
			DataMap dataMap = (DataMap)sapProductList.get(i);
			String currPrdCd = dataMap.getString("MATERIAL");
			if (productDto.getMaterialNo().equals(currPrdCd) )
			{
				resultMsgCd = "OK";
				break;
			}
		}
		
		if ("".equals(resultMsgCd)) 
		{
			resultMsgCd = "NO_MATCH_DATA";
		}
		
		resultMap.put("resultCd", resultMsgCd);
		return resultMap;
	}


	// 사용자 Start

	// 상품 n건 조회(사용자)
	@PostMapping("/list")
	@ResponseBody
	public Object list(@ModelAttribute MerchandizeProductSearchDto productSearchDto)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		productSearchDto.setCorp(paramMap.getCorpName());
		productSearchDto.setProductImagePath( environment.getProperty("file.upload.rootPath") );
		List<MerchandizeProductDto> productResultDtoList = productService.getProductItemsUser(productSearchDto);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("productList", productResultDtoList);
		
		if (productResultDtoList != null)
		{
			resultMap.put("resCnt", productResultDtoList.size());
			
			// 카테고리를 특정하여 검색했을 경우 카테고리 코드 추가
//			if (CmStringUtils.isNotEmpty(productSearchDto.getCategoryCode())) 
//			{
//				paramMap.put("categoryInfo", productSearchDto.getCategoryCode());	// 화면에서는 mcSeq가 넘어오므로 RFC요청 보내기 전 Category Code로 변경 필요
//			}
			
			// 상품명이나 상품코드를 특정하여 검색했을 경우, SAP 요청 파라미터가 달라짐
			if (CmStringUtils.isNotEmpty(productSearchDto.getProductCodeOrName()))
			{
				paramMap.put("materialNo", productResultDtoList.get(0).getMaterialNo());
			}
			RfcResponse rfcResponse = merchandizeGeneralService.getProductInfoBySap(paramMap);
			
			int loopEndCnt = 0;
			DataList sapProductList = rfcResponse.getTable("GT_ZSSS0101");
			for (int i = 0, size = sapProductList.size(); i < size; i++)
			{
				if (loopEndCnt == productResultDtoList.size())
				{
					break;
				}
				
				DataMap dataMap = (DataMap)sapProductList.get(i);
				
				for (MerchandizeProductDto productDto : productResultDtoList)
				{
					if (productDto.getMaterialNo().equals(dataMap.getString("MATNR")))					
					{
						String sapNewPriceStr = dataMap.getString("KBETR");
						double sapNewPrice = CmNumberUtils.toDouble(sapNewPriceStr);
						productDto.setPrice((int)sapNewPrice);
						loopEndCnt++;
						break;
					}
				}			
			}
			
		} else
		{
			resultMap.put("resCnt", 0);
		}
		
		return resultMap;
	}

	// Image preview 대상 데이터 조회(사용자)
	@PostMapping("/previewImage")
	@ResponseBody
	public Object previewImage(@ModelAttribute FileUploadDto fuDto)
	{
		ErpDataMap paramMap = getErpUserInfo();
		
		fuDto.setCorp(paramMap.getCorpName());
		fuDto.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
		fuDto.setType(EuMerchandizeCommonVo.FILE_TYPE_IMAGE);
		List<FileUploadDto> imageFileList = fileUploadService.getFileUploadList(fuDto);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("imageFileList", imageFileList);
		return resultMap;
	}

	// 사용자 End
}
