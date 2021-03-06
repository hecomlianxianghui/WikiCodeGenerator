//
//  UploadStategyRequest.h
//  RedCircleManager
//
//  Generated by WikiCodeGenerator
//  Created by lianxianghui on 16/9/29.
//  Copyright © 2016年 Hecom. All rights reserved.
//

#import "HttpReqBase.h"

/**
 *  参考wiki:
 */
 @interface UploadStategyRequest : HttpReqBase
/**
 *  发送请求
 *
 *  @param successBlock 请求成功回调
 *  @param failureBlock 请求失败回调
 */
- (void)sendWithSuccessBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;
@end