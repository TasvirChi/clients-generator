// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Borhan Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Borhan Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
#import <Foundation/Foundation.h>
#import "ASIHTTPRequestDelegate.h"
#import "ASIProgressDelegate.h"
#import "BorhanXmlParsers.h"

/*
 Constants
 */
#define BORHAN_UNDEF_BOOL      ([BorhanBool UNDEF_VALUE])
#define BORHAN_UNDEF_INT       INT_MIN
#define BORHAN_UNDEF_FLOAT     NAN
#define BORHAN_UNDEF_STRING    (nil)
#define BORHAN_UNDEF_OBJECT    (nil)

#define BORHAN_NULL_BOOL   ([BorhanBool NULL_VALUE])
#define BORHAN_NULL_INT    INT_MAX
#define BORHAN_NULL_FLOAT  INFINITY
#define BORHAN_NULL_STRING (@"__null_string__")
#define BORHAN_NULL_OBJECT ([[[BorhanObjectBase alloc] init] autorelease])

extern NSString* const BorhanClientErrorDomain;

typedef int BORHAN_BOOL;

typedef enum {
    BorhanClientErrorAPIException = 1,
    BorhanClientErrorInvalidHttpCode = 2,
    BorhanClientErrorUnknownObjectType = 3,
    BorhanClientErrorXmlParsing = 4,
    BorhanClientErrorUnexpectedTagInSimpleType = 5,
    BorhanClientErrorUnexpectedArrayTag = 6,
    BorhanClientErrorUnexpectedMultiReqTag = 7,
    BorhanClientErrorMissingMultiReqItems = 8,
    BorhanClientErrorMissingObjectTypeTag = 9,
    BorhanClientErrorExpectedObjectTypeTag = 10,
    BorhanClientErrorExpectedPropertyTag = 11,
    BorhanClientErrorStartTagInSimpleType = 12,
    BorhanClientErrorEmptyObject = 13,
} BorhanClientErrorType;

typedef enum 
{
    KFT_Invalid,
    KFT_Bool,
    KFT_Int,
    KFT_Float,
    KFT_String,
    KFT_Object,
    KFT_Array,
	KFT_Dictionary,
} BorhanFieldType;

/*
 Forward declarations
 */
@protocol BorhanXmlParserDelegate;
@class ASIFormDataRequest;
@class BorhanXmlParserBase;
@class BorhanLibXmlWrapper;
@class BorhanParams;
@class BorhanClientBase;

/*
 Class BorhanBool
 */
@interface BorhanBool : NSObject
+ (BORHAN_BOOL)NO_VALUE;
+ (BORHAN_BOOL)YES_VALUE;
+ (BORHAN_BOOL)NULL_VALUE;
+ (BORHAN_BOOL)UNDEF_VALUE;
@end

/*
 Class BorhanClientException
 */
@interface BorhanClientException : NSException
@end

/*
 Class BorhanSimpleTypeParser
 */
@interface BorhanSimpleTypeParser : NSObject

+ (BORHAN_BOOL)parseBool:(NSString*)aStr;
+ (int)parseInt:(NSString*)aStr;
+ (double)parseFloat:(NSString*)aStr;

@end

/*
 Class BorhanObjectBase
 */
@interface BorhanObjectBase : NSObject

- (void)toParams:(BorhanParams*)aParams isSuper:(BOOL)aIsSuper;

@end

/*
 Class BorhanException
 */
@interface BorhanException : BorhanObjectBase

@property (nonatomic, copy) NSString* code;
@property (nonatomic, copy) NSString* message;

- (NSError*)error;

@end

/*
 Class BorhanObjectFactory
 */
@interface BorhanObjectFactory : NSObject

+ (BorhanObjectBase*)createByName:(NSString*)aName withDefaultType:(NSString*)aDefaultType;

@end

/*
 Class BorhanParams
 */
@interface BorhanParams : NSObject
{
    NSMutableArray* _params;
    NSMutableArray* _files;
    NSMutableString* _prefix;
}

- (void)setPrefix:(NSString*)aPrefix;
- (NSString*)get:(NSString*)aKey;
- (void)putKey:(NSString*)aKey withString:(NSString*)aVal;
- (void)putNullKey:(NSString*)aKey;
- (void)addIfDefinedKey:(NSString*)aKey withFileName:(NSString*)aFileName;
- (void)addIfDefinedKey:(NSString*)aKey withBool:(BORHAN_BOOL)aVal;
- (void)addIfDefinedKey:(NSString*)aKey withInt:(int)aVal;
- (void)addIfDefinedKey:(NSString*)aKey withFloat:(double)aVal;
- (void)addIfDefinedKey:(NSString*)aKey withString:(NSString*)aVal;
- (void)addIfDefinedKey:(NSString*)aKey withObject:(BorhanObjectBase*)aVal;
- (void)addIfDefinedKey:(NSString*)aKey withArray:(NSArray*)aVal;
- (void)addIfDefinedKey:(NSString*)aKey withDictionary:(NSDictionary*)aVal;
- (void)sign;
- (void)addToRequest:(ASIFormDataRequest*)aRequest;
- (void)appendQueryString:(NSMutableString*)output;

@end

/*
 Protocol BorhanLogger
 */
@protocol BorhanLogger <NSObject>

- (void)logMessage:(NSString*)aMsg;

@end

/*
 Class BorhanNSLogger
 */
@interface BorhanNSLogger: NSObject <BorhanLogger>

@end

/*
 Class BorhanServiceBase
 */
@interface BorhanServiceBase : NSObject

@property (nonatomic, assign) BorhanClientBase* client;

- (id)initWithClient:(BorhanClientBase*)aClient;

@end

/*
 Class BorhanClientPlugin
 */
@interface BorhanClientPlugin : NSObject
@end

/*
 Class BorhanConfiguration
 */
@interface BorhanConfiguration : NSObject

@property (nonatomic, copy) NSString* serviceUrl;
@property (nonatomic, copy) NSString* clientTag;
@property (nonatomic, assign) int partnerId;
@property (nonatomic, assign) int requestTimeout;
@property (nonatomic, retain) id<BorhanLogger> logger;
@property (nonatomic, copy) NSDictionary* requestHeaders;

@end

/*
 Protocol BorhanClientDelegate
 */
@protocol BorhanClientDelegate

- (void)requestFinished:(BorhanClientBase*)aClient withResult:(id)result;
- (void)requestFailed:(BorhanClientBase*)aClient;

@end

/*
 Class BorhanClientBase
 */
@interface BorhanClientBase : NSObject <ASIHTTPRequestDelegate, BorhanXmlParserDelegate>
{
    BOOL _isMultiRequest;
    BorhanXmlParserBase* _reqParser;
    BorhanXmlParserBase* _skipParser;
    ASIFormDataRequest *_request;
    BorhanLibXmlWrapper* _xmlParser;
    NSDate* _apiStartTime;
}

@property (nonatomic, retain) BorhanConfiguration* config;
@property (nonatomic, retain) NSError* error;
@property (nonatomic, assign) id<BorhanClientDelegate> delegate;
@property (nonatomic, assign) id<ASIProgressDelegate> uploadProgressDelegate;
@property (nonatomic, assign) id<ASIProgressDelegate> downloadProgressDelegate;
@property (nonatomic, copy) NSString* ks;
@property (nonatomic, copy) NSString* apiVersion;
@property (nonatomic, readonly) BorhanParams* params;
@property (nonatomic, readonly) NSDictionary* responseHeaders;

    // public messages
- (id)initWithConfig:(BorhanConfiguration*)aConfig;
- (void)startMultiRequest;
- (NSArray*)doMultiRequest;
- (void)cancelRequest;
+ (NSString*)generateSessionWithSecret:(NSString*)aSecret withUserId:(NSString*)aUserId withType:(int)aType withPartnerId:(int)aPartnerId withExpiry:(int)aExpiry withPrivileges:(NSString*)aPrivileges;

    // messages for use of auto-gen service code
- (NSString*)queueServeService:(NSString*)aService withAction:(NSString*)aAction;
- (void)queueVoidService:(NSString*)aService withAction:(NSString*)aAction;
- (BORHAN_BOOL)queueBoolService:(NSString*)aService withAction:(NSString*)aAction;
- (int)queueIntService:(NSString*)aService withAction:(NSString*)aAction;
- (double)queueFloatService:(NSString*)aService withAction:(NSString*)aAction;
- (NSString*)queueStringService:(NSString*)aService withAction:(NSString*)aAction;
- (id)queueObjectService:(NSString*)aService withAction:(NSString*)aAction withExpectedType:(NSString*)aExpectedType;
- (NSMutableArray*)queueArrayService:(NSString*)aService withAction:(NSString*)aAction withExpectedType:(NSString*)aExpectedType;

@end
