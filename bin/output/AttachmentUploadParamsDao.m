//
//  AttachmentUploadParamsDao.m
//  RedCircleManager
//
//  Generated by WikiCodeGenerator
//  Created by lianxianghui on 16/9/23.
//  Copyright © 2016年 Hecom. All rights reserved.
//

#import "AttachmentUploadParamsDao.h"
#import "AttachmentUploadParams.h"
#import "SqliteManager.h"

@implementation AttachmentUploadParamsDao
+ (int)createTable {
	NSString *sql = @"create table if not exists AttachmentUploadParams (uploadType text primary key,bucketName text,endPoint text,imgEndPoint text,objectKey text,accessKeyId text,accessKeySecret text,securityToken text,callbackUrl text,callbackHost text)";
    return [SqliteManager sqlite3ExecCommand:sql];
}

+ (int)saveAttachmentUploadParams:(AttachmentUploadParams *)obj
{
    NSMutableDictionary *recordDic = [NSMutableDictionary dictionary];
    [recordDic setValue:obj.uploadType forKey:@"uploadType"];
    [recordDic setValue:obj.bucketName forKey:@"bucketName"];
    [recordDic setValue:obj.endPoint forKey:@"endPoint"];
    [recordDic setValue:obj.imgEndPoint forKey:@"imgEndPoint"];
    [recordDic setValue:obj.objectKey forKey:@"objectKey"];
    [recordDic setValue:obj.accessKeyId forKey:@"accessKeyId"];
    [recordDic setValue:obj.accessKeySecret forKey:@"accessKeySecret"];
    [recordDic setValue:obj.securityToken forKey:@"securityToken"];
    [recordDic setValue:obj.callbackUrl forKey:@"callbackUrl"];
    [recordDic setValue:obj.callbackHost forKey:@"callbackHost"];
   return [SqliteManager writeAllRecord:@[recordDic] databaseTableName:@"AttachmentUploadParams" userIdentify:[GlobalPathMacro getUserIdentify]];
}

+ (int)saveAttachmentUploadParamsList:(NSArray *)objList
{
    NSMutableArray *recordList = [NSMutableArray new];
    for (AttachmentUploadParams *obj in objList) {
    	NSMutableDictionary *recordDic = [NSMutableDictionary dictionary];
    	[recordDic setValue:obj.uploadType forKey:@"uploadType"];
    	[recordDic setValue:obj.bucketName forKey:@"bucketName"];
    	[recordDic setValue:obj.endPoint forKey:@"endPoint"];
    	[recordDic setValue:obj.imgEndPoint forKey:@"imgEndPoint"];
    	[recordDic setValue:obj.objectKey forKey:@"objectKey"];
    	[recordDic setValue:obj.accessKeyId forKey:@"accessKeyId"];
    	[recordDic setValue:obj.accessKeySecret forKey:@"accessKeySecret"];
    	[recordDic setValue:obj.securityToken forKey:@"securityToken"];
    	[recordDic setValue:obj.callbackUrl forKey:@"callbackUrl"];
    	[recordDic setValue:obj.callbackHost forKey:@"callbackHost"];
    	[recordList addObject:recordDic];
    }
    return [SqliteManager writeAllRecord:recordList databaseTableName:@"AttachmentUploadParams" userIdentify:[GlobalPathMacro getUserIdentify]];
}

+ (int)deleteAttachmentUploadParamsWithPrimaryKeyValue:(NSString *)pkValue
{
    NSString *strSql = [NSString stringWithFormat:@"delete from AttachmentUploadParams where uploadType='%@'", pkValue];
    return [SqliteManager sqlite3ExecCommand:strSql];
}

+ (int)deleteAllAttachmentUploadParams
{
    NSString *strSql = @"delete from AttachmentUploadParams";
    return [SqliteManager sqlite3ExecCommand:strSql];
}

+ (NSMutableArray *)fetchAttachmentUploadParamsListWithOffset:(NSInteger)offset limit:(NSInteger)limit {
	NSMutableArray *objArray = [NSMutableArray array];
    NSString *sql = [NSString stringWithFormat:@"select * from AttachmentUploadParams limit %@ offset %@", @(offset), @(limit)]; 
    NSMutableArray *recordList = [SqliteManager getRecordsWithSql:sql];
    if (recordList.count > 0) {
        for (NSDictionary *record in recordList) {
            AttachmentUploadParams *obj = [AttachmentUploadParams new];
            obj.uploadType = record[@"uploadType"];
            obj.bucketName = record[@"bucketName"];
            obj.endPoint = record[@"endPoint"];
            obj.imgEndPoint = record[@"imgEndPoint"];
            obj.objectKey = record[@"objectKey"];
            obj.accessKeyId = record[@"accessKeyId"];
            obj.accessKeySecret = record[@"accessKeySecret"];
            obj.securityToken = record[@"securityToken"];
            obj.callbackUrl = record[@"callbackUrl"];
            obj.callbackHost = record[@"callbackHost"];
            [objArray addObject:obj];
        }
    } 
    return objArray;
}

@end