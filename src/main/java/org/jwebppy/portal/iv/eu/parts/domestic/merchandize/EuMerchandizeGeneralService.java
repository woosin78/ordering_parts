package org.jwebppy.portal.iv.eu.parts.domestic.merchandize;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileUploadEntity;
import org.jwebppy.portal.iv.eu.common.file_upload.mapper.EuFileUploadMapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeCategoryDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeGeneralDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeRecommendProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeCategoryEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeGeneralEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeProductEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeCategoryMapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeGeneralMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

public class EuMerchandizeGeneralService
{
	@Autowired
	private EuMerchandizeGeneralMapper merchandizeGeneralMapper;

	@Autowired
	private EuMerchandizeCategoryMapper merchandizeCategoryMapper;

	@Autowired
	private EuFileUploadMapper fileUploadMapper;

	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@Autowired
	private Environment environment;

	// 카테고리 1건 등록 또는 수정 분기
	@Transactional
	public int changeSortNumber(MerchandizeGeneralDto dto)
	{
		int updateTargetCnt = 0;	// 변경 주체 1건 갱신 카운트
		int updateEtcCnt = 0;	// 변경 영향을 받는 다수 건 갱신 카운트

		MerchandizeGeneralEntity updateTargetEntity = merchandizeGeneralMapper.findUpdateSortInfo(CmModelMapperUtils.map(dto, MerchandizeGeneralEntity.class));
		String[] updateSeqArr = updateTargetEntity.getTSeqStrArray().split(",");
		dto.setTSeqs(updateSeqArr);
//
		updateEtcCnt = merchandizeGeneralMapper.updateSortNumbers(dto);		// 위 1건에 의해 정렬순서가 재조정되는 영향을 받는 모든 건수 갱신

		updateTargetCnt = merchandizeGeneralMapper.updateSortNumber(dto);	// 관리자가 정렬순서 변경 액션을 취한 주체 1건 갱신

		return updateEtcCnt + updateTargetCnt;
	}


	// 썸네일을 생성하고 물리 위치에 저장한 후, 파일 테이블에 관련정보를 저장
	protected void createThumbnailImage(FileUploadEntity orgImageInfoEntity, MerchandizeProductEntity mpEntity)
	{
		FileUploadEntity fuEntity = null;
		try
		{
			int thumbnail_width = EuMerchandizeCommonVo.FILE_SIZE_MALL_THUMBNAIL_W;	// 썸네일 가로사이즈
			int thumbnail_height = EuMerchandizeCommonVo.FILE_SIZE_MALL_THUMBNAIL_H;	// 썸네일 세로사이즈

			String ext = orgImageInfoEntity.getExtension();
			String newFileName = orgImageInfoEntity.getOriginName().replace("." + ext, ".thumbnail." + ext);	// 화면에 표시되는 썸네일 파일 이름
			String newFileSavedName =  orgImageInfoEntity.getSavedName().substring(0, orgImageInfoEntity.getSavedName().lastIndexOf(".")) + ".thumbnail." + ext;	// 서버에 저장되는 썸네일 파일 이름

			File originFile = new File(orgImageInfoEntity.getStoragePath() + orgImageInfoEntity.getSavedName());
			
			BufferedImage buffer_original_image = ImageIO.read(originFile);

			int imgType = (buffer_original_image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
	        BufferedImage buffer_thumbnail_image = new BufferedImage(thumbnail_width, thumbnail_height, imgType);
	        Graphics2D graphic = buffer_thumbnail_image.createGraphics();

	        graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        graphic.drawImage(buffer_original_image, 0, 0, thumbnail_width, thumbnail_height, null);

	        long thumbnailSize = 0;
	        if (ext.equalsIgnoreCase("jpg"))
	        {
	        	thumbnailSize = writeJpeg(buffer_thumbnail_image, new File(orgImageInfoEntity.getStoragePath() + newFileSavedName), 1.0f);
	        } else
	        {
	        	File thumb_file_name = new File(orgImageInfoEntity.getStoragePath() + newFileSavedName);
	            
	            ImageIO.write(buffer_thumbnail_image, ext.toLowerCase(), thumb_file_name);	// 이미지 생성
	            thumbnailSize = thumb_file_name.length();
	        }
	        graphic.dispose();	// 썸네일 이미지 처리 끝

	        fuEntity = new FileUploadEntity();
	        fuEntity.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
	        fuEntity.setTSeq(mpEntity.getMpSeq());
	        fuEntity.setOriginName(newFileName);		// 실제 파일
	        fuEntity.setSavedName(newFileSavedName);	// 저장할 파일
	        fuEntity.setExtension(ext.toLowerCase());
	        fuEntity.setFileSize(Math.toIntExact(thumbnailSize));
	        fuEntity.setFgDelete(PlatformCommonVo.NO);
	        fuEntity.setType(EuMerchandizeCommonVo.FILE_TYPE_THUMBNAIL);
	        fuEntity.setStoragePath(getMerchandizeFileUploadPath());

	        fileUploadMapper.insertFileUpload(fuEntity);

		} catch (Exception e) {
			e.printStackTrace();	
		}
	}


	// jpeg 파일을 생성한다.
	private long writeJpeg(BufferedImage image, File destFile, float quality) throws IOException 
	{
	    ImageWriter writer = null;
	    FileImageOutputStream output = null;
	    long fileLength = 0;
	    
	    try 
	    {
	        writer = ImageIO.getImageWritersByFormatName("jpeg").next();
	        ImageWriteParam param = writer.getDefaultWriteParam();

	        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	        param.setCompressionQuality(quality);

	        output = new FileImageOutputStream(destFile);
	        writer.setOutput(output);

	        IIOImage iioImage = new IIOImage(image, null, null);
	        writer.write(null, iioImage, param);
	        
	        fileLength = output.length();
	    } catch (IOException ex) 
	    {
	        throw ex;
	    } finally 
	    {
	        if (writer != null) 
	        {
	            writer.dispose();
	        }

	        if (output != null) 
	        {
	            output.close();
	        }
	    }

	    return fileLength;
	}


	// 카테고리 정보 리스트(RFC)
	public RfcResponse getCategoryInfoBySap()
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_PRO_HIERCHY_MD");
		rfcRequest.addField("I_SPRAS", "EN");
		rfcRequest.addField("I_STUFE", "3");

		return simpleRfcTemplate.response(rfcRequest);
	}


	/**
	 * 상품 정보 리스트(RFC)
	 *
	 * @param paramMap PartsGenernalController의 getUserInfo() 메서드에서 반환된 유저 정보 DataMap에 다음 추가정보가 포함됨:
	 *                 1. 카테고리 파라미터정보DTO(MerchandizeCategoryDto) : 카테고리 코드 String형 변수에 카테고리 배열정보가 있음. (ex: 검색대상 카테고리 코드가 1부터 3까지일 경우  getCategoryCode() = [1,2,3])
	 *                 2. 검색화면에서 상품명이나 상품정보를 특정하여 검색했을 경우, 해당 상품코드를 찾은 후 materialNo 를 key 로 상품청보를 추가
	 * @return RfcResponse
	 */
	@SuppressWarnings("unchecked")
	public RfcResponse getProductInfoBySap(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MARA_MD");

		// 1. SAP상품정보를 가져오는 함수 [ZSS_PARA_DIV_EP_MARA_MD] 의 RFC 요청정보 파라미터에 기본적으로 포함되는 변수들
		rfcRequest.addField("I_LANGU", paramMap.getLang());	
		rfcRequest.addField("I_PRICE_FLAG", "X");
		rfcRequest.addField("I_KVGR5", paramMap.getCustomerGrp5());
		rfcRequest.addField("I_LINE_CNT", "100");
		rfcRequest.addField("I_CURRENT_PAGE", "1");
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		// 2. SAP상품정보를 가져오는 함수 [ZSS_PARA_DIV_EP_MARA_MD] 의 RFC 요청정보 파라미터 중 카테고리 범위만을 세팅하는 부분
		MerchandizeCategoryDto categoryDto = new MerchandizeCategoryDto();
		categoryDto.setCorp(paramMap.getCorpName());
		categoryDto.setCategoryLang(paramMap.getLang());
		List<MerchandizeCategoryEntity> categoryEntities = merchandizeCategoryMapper.findCategoryItems(categoryDto);

		ArrayList<HashMap<String, String>> cateInputList = new ArrayList<HashMap<String, String>>();
		// paramMap 의 카테고리 정보는 mcSeq값이므로, 해당 값과 일치하는 카테고리 정보를 찾아 그 정보의 카테고리코드를 적재함.
		if (CmStringUtils.isNotEmpty(paramMap.getString("categoryInfo")))
		{
			for (MerchandizeCategoryEntity entity : categoryEntities)
			{
				if (paramMap.get("categoryInfo").equals(entity.getMcSeq().toString()))
				{
					HashMap<String, String> cateMap = new HashMap<String, String>();
					cateMap.put("PRODH", entity.getCategoryCode());
					cateInputList.add(cateMap);
				}
			}
		} else
		{
			for (MerchandizeCategoryEntity entity : categoryEntities)
			{
				HashMap<String, String> cateMap = new HashMap<String, String>();
				cateMap.put("PRODH", entity.getCategoryCode());
				cateInputList.add(cateMap);
			}
		}
		rfcRequest.addTable("GT_PRODH", cateInputList);

		
		ArrayList<Object> inputList = new ArrayList<Object>();
		// 3. 상품코드를 특정하여 검색했을 경우에만 포함되는 검색용 테이블
		if ( CmStringUtils.isNotEmpty(paramMap.getString("materialNo")) )
		{
			
			HashMap<String,String> inputTable = new HashMap<String,String>();
			inputTable.put("MATNR", paramMap.getString("materialNo"));

			// TODO 한국 머천다이즈 테스트 코드(정식 릴리즈 때는 아래쪽 2줄을 풀고, 하드코딩된 위쪽 2줄을 주석처리할 것
//			inputTable.put("VKORG", "F116");
//			inputTable.put("VTWEG", "10");
			inputTable.put("VKORG", paramMap.getString("VKORG"));
			inputTable.put("VTWEG", paramMap.getString("VTWEG"));

			inputList.add(inputTable);
			rfcRequest.addTable("GT_ZSSS0101", inputList);
		// 추천상품 복수개의 가격관련정보를 가져올 때의 처리	
		} else if (CmStringUtils.isNotEmpty(paramMap.getString("recommandDtos")))
		{
			List<MerchandizeRecommendProductDto> recommandDtos =  (List<MerchandizeRecommendProductDto>)paramMap.get("recommandDtos");
			for (MerchandizeRecommendProductDto rePrdDto : recommandDtos)
			{
				HashMap<String,String> inputTable = new HashMap<String,String>();
				inputTable.put("MATNR", rePrdDto.getMaterialNo());
				inputTable.put("VKORG", paramMap.getString("VKORG"));
				inputTable.put("VTWEG", paramMap.getString("VTWEG"));
				inputList.add(inputTable);
			}
			rfcRequest.addTable("GT_ZSSS0101", inputList);
		}

		return simpleRfcTemplate.response(rfcRequest);
	}
	
	
	// 상품 유효성 체크(like 검색결과를 반환하므로, 응답 데이터리스트에서 맞는 상품코드 체크 필수) 
	public RfcResponse checkValidProductBySap(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY2");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		
		ArrayList<Object> inputList = new ArrayList<Object>();
		HashMap<String,String> inputTable = new HashMap<String,String>();
		inputTable.put("MATERIAL", paramMap.getString("materialNo"));
		inputList.add(inputTable);
		rfcRequest.addTable("LT_ITEM", inputList);
		
		return simpleRfcTemplate.response(rfcRequest);
	}
	

	// 파일 경로 반환
	protected String getMerchandizeFileUploadPath()
	{
		File path = new File(environment.getProperty("file.upload.rootPath") + EuMerchandizeCommonVo.MERCHANDIZE_FILE_PATH);

		if (!path.exists())
		{
			path.mkdir();
		}
		return EuMerchandizeCommonVo.MERCHANDIZE_FILE_PATH;	// 파일 업로드 서비스에서 파일저장 절대경로를 만들고 파일dto의 storagePath경로부분만을 붙여서 파일의 경로를 만들고 있으므로, 머천다이즈 세부 경로만을 반환.
	}
}
